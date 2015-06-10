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
package com.github.ipaas.ifw.core.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.github.ipaas.ifw.core.config.Config;
import com.github.ipaas.ifw.core.config.FwConfigService;

/**
 * @author wudie 服务实例工厂类
 */
public class ServiceFactory {

	private static Lock INIT_LOCK = new ReentrantLock();

	private static ServiceProvider serviceProvider;

	public static void setServiceProvider(ServiceProvider provider) {
		serviceProvider = provider;
	}

	/**
	 * 初始化
	 */
	public static void init() {
		// doubleCheck+lock保证线程安全和性能
		if (serviceProvider == null) {
			try {
				INIT_LOCK.lock();
				if (serviceProvider == null) {
					Config sysConfig = FwConfigService.getSystemConfig();
					String sp = sysConfig.getItem("service.provider", "spring");
					serviceProvider = ServiceProviderFactory.getServiceProvider(sp);
				}
			} finally {
				INIT_LOCK.unlock();
			}
		}
	}

	/**
	 * 获取服务
	 * 
	 * @param serviceId
	 * @return
	 */
	public static <T> T getService(String serviceID) {
		init();
		return (T) serviceProvider.getService(serviceID);
	}

}
