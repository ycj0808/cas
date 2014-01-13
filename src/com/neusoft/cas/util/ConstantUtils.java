package com.neusoft.cas.util;

/**
 * 常量
 * @author yangchj
 * @date 2013-12-25 
 */
public class ConstantUtils {
	/********************用于sharePreference的常量-start********************/
	public final static String FIRST_LOGIN="FirstLogin";
	public final static String LAST_UPDATE_TIME="last_update_time";
	public final static String SESSION_ID="jsessionid";
	public final static String REMEMBER_PASSWORD="rem_password";
	public final static String LOGIN_ACCOUNT="login_account";
	public final static String LOGIN_PASSWORD="login_password";
	
	/********************用于sharePreference的常量-end********************/
	
	/********************用于监听网络连接的常量-start********************/
	public final static int NOT_HAS_NET=101;//无网络
	public final static int HAS_WIFI=102;	//Wifi可用
	public final static int HAS_MOBILE=103; //手机网络可用
	
	public static boolean IS_WIFI=false;//Wifi可用
	public static boolean IS_MOBILE=false;//手机网络可用
	
	public static String STR_COMMON_URL="http://10.4.127.65:8080/framework/unieapServices/rest/";
	public static String STR_LOGIN_URL="http://10.4.127.65:8080/framework/techcomp/ria/commonProcessor!login.action";
	public static String STR_BASE_URL="http://10.4.127.65:8080/framework/";
	/********************用于监听网络连接的常量-end********************/
	
	/********************注册加载数据常量-start********************/
	public final static int LADING_DATA=1001;//加载数据
	
	/********************注册加载数据常量-end********************/
	
	/********************注册加载数据常量-start********************/
	public final static int LOGIN_ERROR1=2001;//用户名或密码为空
	public final static int LOGIN_ERROR2=2002;//用户名或密码不正确
	public final static int LOGIN_ERROR3=2003;//无网络
	public final static int LOGIN_SUCCESS=2004;//登陆成功
	/********************注册加载数据常量-end********************/
}
