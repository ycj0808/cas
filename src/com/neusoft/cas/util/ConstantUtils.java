package com.neusoft.cas.util;

/**
 * 常量
 * @author yangchj
 * @date 2013-12-25 
 */
public class ConstantUtils {
	/********************用于sharePreference的常量-start********************/
	public static String FIRST_LOGIN="FirstLogin";
	public static String LAST_UPDATE_TIME="last_update_time";
	public static String SESSION_ID="jsessionid";
	
	/********************用于sharePreference的常量-end********************/
	
	/********************用于监听网络连接的常量-start********************/
	public final static int NOT_HAS_NET=101;//无网络
	public final static int HAS_WIFI=102;	//Wifi可用
	public final static int HAS_MOBILE=103; //手机网络可用
	
	public static boolean IS_WIFI=false;//Wifi可用
	public static boolean IS_MOBILE=false;//手机网络可用
	/********************用于监听网络连接的常量-end********************/
	
	/********************注册加载数据常量-start********************/
	public final static int LADING_DATA=1001;
	
	/********************注册加载数据常量-end********************/
}
