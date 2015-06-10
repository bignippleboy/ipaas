/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ipaas.ifw.mq.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ipaas.ifw.core.exception.FwRuntimeException;
import com.github.ipaas.ifw.core.mbean.Fw;
import com.github.ipaas.ifw.mq.Message;
import com.github.ipaas.ifw.mq.MqSendService;
import com.github.ipaas.ifw.mq.mbean.MqHealthCheck;
import com.github.ipaas.ifw.util.JMXUtil;
import com.github.ipaas.ifw.util.JsonUtil;

/**
 * @author Chenql
 */
public class FwMqSendService implements MqSendService {

	private static Logger logger = LoggerFactory.getLogger(FwMqSendService.class);

	/**
	 * 连接池工厂
	 */
	private PooledConnectionFactory producerPooledFactory;

	/**
	 * 生产信息的brokerUrl
	 */
	private String brokerForProduct;

	/**
	 * 连接超时时间,单位ms,默认是1秒
	 */
	private int connectTimeout = 1000;

	/**
	 * 操作超时时间,单位ms,默认操作超时时间是5秒
	 */
	private int operateTimeout = 5000;

	/**
	 * 连接池尺寸，默认是5
	 */
	private int maximumConnections = 5;

	/**
	 * 每个连接中最多Session数，默认是500
	 */
	private int maximumActive = 500;

	/**
	 * 连接闲置超时时间,单位ms,默认是1分钟
	 */
	private int idleTimeout = 60000;

	/**
	 * 服务器列表
	 */
	private String serverUrl;

	/**
	 * 认证和授权的用户名
	 */
	private String userName;
	/**
	 * 认证和授权的密码
	 */
	private String password;
	/**
	 * 服务Id
	 */
	private String serviceId;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setOperateTimeout(int operateTimeout) {
		this.operateTimeout = operateTimeout;
	}

	public void setMaximumConnections(int maximumConnections) {
		this.maximumConnections = maximumConnections;
	}

	public void setMaximumActive(int maximumActive) {
		this.maximumActive = maximumActive;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 从连接池中取得连接，如果连接池为null，则初始化连接池
	 * <p>
	 * 由于连接池中取得的连接会自动回收，因此该连接不能用于Consumer，池连接主要用于Producer
	 * 
	 * @return Connection
	 * @throws JMSException
	 */
	private Connection getPooledConnection() throws JMSException {
		if (null == producerPooledFactory) {
			// 初始化连接池工厂并设置参数
			synchronized (this) {
				if (serverUrl == null || serverUrl.trim().equals("")) {
					throw new FwRuntimeException("未设置服务器地址serverUrl");
				}

				if (brokerForProduct == null) {
					StringBuilder brokerUrlSb = new StringBuilder();
					brokerUrlSb.append("failover:(");
					String[] hostArr = serverUrl.split(",");
					for (String host : hostArr) {
						brokerUrlSb.append("tcp://" + host + "?connectionTimeout=" + connectTimeout).append(",");
					}
					// 删除最后多出来的,号
					brokerUrlSb.deleteCharAt(brokerUrlSb.length() - 1);
					brokerUrlSb.append(")?timeout=" + operateTimeout);
					brokerForProduct = brokerUrlSb.toString();
				}

				if (producerPooledFactory == null) {
					ActiveMQConnectionFactory connectionFactory = FwMqServiceHelper.createConnectionFactory(
							brokerForProduct, userName, password);

					PooledConnectionFactory pcf = new PooledConnectionFactory(connectionFactory);
					// 设置连接池尺寸
					pcf.setMaxConnections(maximumConnections);
					// 设置每个连接的最多Session数
					pcf.setMaximumActive(maximumActive);
					// 设置连接闲置超时回收时间
					pcf.setIdleTimeout(idleTimeout);

					producerPooledFactory = pcf;
				}

				initMqHealthMBean();// 注册MBean
			}
		}
		return producerPooledFactory.createConnection();
	}

	/**
	 * 根据传输类型进行不同方式的发送处理
	 * 
	 * @param name
	 *            Queue/Topic的名字
	 * @param message
	 *            发送的消息
	 * @param type
	 *            传输类型
	 */
	private void send(String name, Message message, TRANSPORT_MODEL type) {
		Connection connection = null;
		Session session = null;
		try {
			// 从连接池取得连接
			connection = getPooledConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 根据传输模式创建Destination
			Destination dest = null;
			if (type == TRANSPORT_MODEL.QUEUE) {
				dest = session.createQueue(name);
			} else {
				dest = session.createTopic(name);
			}

			// 创建Producer
			MessageProducer producer = session.createProducer(dest);
			// 创建JMS Message
			javax.jms.Message jmsMsg = null;
			Object content = message.getContent();
			if (content instanceof String) {
				// String类型内容不需要转换
				TextMessage textMsg = session.createTextMessage();
				textMsg.setText((String) content);
				jmsMsg = textMsg;
			} else if (message.isAutoJsonConvert()) {
				// 进行自动的Json转换
				TextMessage textMsg = session.createTextMessage();
				textMsg.setText(JsonUtil.toJson(content));
				jmsMsg = textMsg;
			} else {
				// 直接序列化
				ObjectMessage objMsg = session.createObjectMessage();
				objMsg.setObject((Serializable) content);
				jmsMsg = objMsg;
			}
			jmsMsg.setStringProperty("MessageID", message.getId());
			int deliveryMode = message.isPersist() ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT;
			// 发送Message
			producer.send(jmsMsg, deliveryMode, message.getPriority(), message.getTimeToLive());

			logger.debug("消息发送完成:" + message.getContent());
		} catch (Exception e) {
			throw new RuntimeException("发送消息失败  " + type + "[" + name + "]", e);
		} finally {
			releaseResource(session, connection);
		}
	}

	/**
	 * 安全的释放资源
	 * 
	 * @param session
	 * @param connection
	 */
	private void releaseResource(Session session, Connection connection) {
		try {
			if (null != session) {
				session.close();
			}
			if (null != connection) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("releaseResource exception cause: ", e);
		}
	}

	@Override
	public Message createMessage() {
		return FwMqServiceHelper.createMessage();
	}

	@Override
	public void sendQueue(String queueName, Message message) {
		send(queueName, message, TRANSPORT_MODEL.QUEUE);

	}

	@Override
	public void sendTopic(String topicName, Message message) {
		send(topicName, message, TRANSPORT_MODEL.TOPIC);
	}

	/**
	 * 
	 * 初始化MQ可用性检测的MBean
	 * 
	 * @return void
	 */
	private void initMqHealthMBean() {
		MqHealthCheck mqHealthCheck;
		try {
			// 拆分主从配置
			String[] group = this.serverUrl.split(",");
			for (int i = 0; i < group.length; i++) {
				String[] ipPort = group[i].split(":");
				mqHealthCheck = new MqHealthCheck(serviceId, ipPort[0], ipPort[1], userName, password);
				String oname = JMXUtil.createObjectNameString(Fw.FW_USABILITY_DOMAIN, MqHealthCheck.TYPE, serviceId,
						ipPort[0] + "-" + ipPort[1]);
				JMXUtil.registerMBean(mqHealthCheck, oname);
				logger.info(" registerMBean for MqHealthCheck " + group[i]);
				mqHealthCheck = null;
			}

		} catch (Throwable e) {
			logger.error("initMqHealthMBean error", e);
		}

	}
}
