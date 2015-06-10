package com.github.ipaas.ifw.jdbc;

/**
 * 数据访问服务管理器
 * 
 * @author Chenql
 */
public final class DataAccessServiceManager {

	// /**
	// * 数据库访问服务插件配置名称
	// */
	// private static String DB_ACCESS_SERVICE_PLUGIN = "db_access_service";
	//
	// /**
	// * 数据库连接服务插件配置名称
	// */
	// private static String DB_CONNECT_SERVICE_PLUGIN = "db_connect_service";
	//
	// /**
	// * 插件服务
	// */
	// private static FwPluginService ps = FwPluginService.newInstance();
	//
	// /**
	// * 数据访问服务集合
	// */
	// private static Map<String, DbAccessService> das = new
	// ConcurrentHashMap<String, DbAccessService>();
	//
	// /**
	// * 数据库连接服务集合
	// */
	// private static Map<String, DbConnectService> dbcs = new
	// ConcurrentHashMap<String, DbConnectService>();
	//
	// /**
	// * 根据appId获取数据访问服务实例
	// * @param appId -- 应用ID
	// * @return -- 数据访问服务对象
	// */
	// public static DbAccessService getDbAccessService(String appId) {
	//
	// DbAccessService da = das.get(appId);
	// if (null == da) {
	// da = ps.getPluginInstance(DB_ACCESS_SERVICE_PLUGIN);
	// // 初始化插件
	// Pluggable plugin = (Pluggable)da;
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("appId", appId);
	// plugin.initializePlugin(params);
	// das.put(appId, da);
	// }
	// return da;
	// }
	//
	// /**
	// * 根据appId获取数据库连接服务实例
	// * @param appId -- 应用ID
	// * @return -- 数据库连接服务对象
	// */
	// public static DbConnectService getDbConnectService(String appId) {
	//
	// DbConnectService dbc = dbcs.get(appId);
	// if (null == dbc) {
	// dbc = ps.getPluginInstance(DB_CONNECT_SERVICE_PLUGIN);
	// // 初始化插件
	// Pluggable plugin = (Pluggable)dbc;
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("appId", appId);
	// plugin.initializePlugin(params);
	// dbcs.put(appId, dbc);
	// }
	// return dbc;
	// }
}
