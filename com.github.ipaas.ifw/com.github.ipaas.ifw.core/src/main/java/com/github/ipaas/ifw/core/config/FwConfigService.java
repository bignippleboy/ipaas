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
package com.github.ipaas.ifw.core.config;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ipaas.ifw.core.exception.FwRuntimeException;
import com.github.ipaas.ifw.util.CloseUtil;
import com.github.ipaas.ifw.util.XmlUtil;

/**
 * 配置服务类
 * 
 * @author Chenql
 */
public final class FwConfigService {

	/**
	 * logger
	 */
	private static Logger logger = LoggerFactory.getLogger(FwConfigService.class);

	private static volatile Map<String, Object> CONFIG_CACHE;

	private final static String CONFIG_FILE_PATH = "/fw_config.xml";

	/**
	 * 加载配置
	 */
	private static void loadConfig() {
		if (CONFIG_CACHE != null) {
			return;
		}

		InputStream ins = null;
		try {
			ins = FwConfigService.class.getResourceAsStream(CONFIG_FILE_PATH);
			if (ins == null) {
				logger.warn(CONFIG_FILE_PATH + "文件不存在");
				return;
			}

			Map<String, Object> xmlMap = XmlUtil.toMap(ins);
			CONFIG_CACHE = xmlMap;
		} catch (Exception e) {
			throw new FwRuntimeException(e);
		} finally {
			CloseUtil.closeSilently(ins);
		}
	}

	/**
	 * 获取配置
	 * 
	 * @param configName
	 * @return
	 */
	public static Map<String, Object> getConfig(String configName) {
		loadConfig();
		if (CONFIG_CACHE == null) {
			return null;
		}
		Map<String, Object> config = (Map<String, Object>) CONFIG_CACHE.get(configName);
		return config;
	}

	/**
	 * 获取系统配置项
	 * 
	 * @return
	 */
	public static Config getSystemConfig() {
		Map<String, Object> dataMap = getConfig("system_properties");
		Config config = new XmlConfig(dataMap);
		return config;
	}

	/**
	 * 获取应用配置项
	 * 
	 * @return
	 */
	public static Config getAppConfig() {
		Map<String, Object> dataMap = getConfig("app_properties");
		Config config = new XmlConfig(dataMap);
		return config;
	}
}
