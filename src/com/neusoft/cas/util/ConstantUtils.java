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
	
	public final static String SERVICE_ADDR="service_addr";
	/********************用于sharePreference的常量-end********************/
	
	/********************用于监听网络连接的常量-start********************/
	public final static int NOT_HAS_NET=101;//无网络
	public final static int HAS_WIFI=102;	//Wifi可用
	public final static int HAS_MOBILE=103; //手机网络可用
	
	public static boolean IS_WIFI=false;//Wifi可用
	public static boolean IS_MOBILE=false;//手机网络可用
	
//	public static String STR_COMMON_URL="http://10.4.127.65:8080/framework/unieapServices/rest/";
//	public static String STR_LOGIN_URL="http://10.4.127.65:8080/framework/techcomp/ria/commonProcessor!login.action";
//	public static String STR_BASE_URL="http://10.4.127.65:8080/framework/";
	
	public static String STR_BASE_URL="http://10.122.201.20:8080/cas";
	
//	public static String STR_COMMON_URL="http://10.4.120.103:8080/cas/unieapServices/rest/";
//	public static String STR_LOGIN_URL="http://10.4.120.103:8080/cas/techcomp/ria/commonProcessor!login.action";
//	public static String STR_BASE_URL="http://10.4.120.103:8080/cas/";
	
//	public static String STR_COMMON_URL="http://10.4.126.145:8080/cas/unieapServices/rest/";
//	public static String STR_LOGIN_URL="http://10.4.126.145:8080/cas/techcomp/ria/commonProcessor!login.action";
//	public static String STR_BASE_URL="http://10.4.126.145:8080/cas/";
	
	public static String LOGIN_URL_SUFFIX="techcomp/ria/commonProcessor!login.action";
	public static String COMMON_URL_SUFFIX="unieapServices/rest/";
	/********************用于监听网络连接的常量-end********************/
	
	/********************注册加载数据常量-start********************/
	public final static int LADING_DATA=1001;//加载数据
	public final static int CHECK_DATA_ERROR1=1002;//用户姓名不能为空
	public final static int REGISTER_SUCCESS=1003;//注册成功
	/********************注册加载数据常量-end********************/
	
	/********************登陆加载数据常量-start********************/
	public final static int LOGIN_ERROR1=2001;//用户名或密码为空
	public final static int LOGIN_ERROR2=2002;//用户名或密码不正确
	public final static int LOGIN_ERROR3=2003;//解析数据出错
	public final static int LOGIN_SUCCESS=2004;//登陆成功
	/********************登陆加载数据常量-end********************/
	
	/********************修改用户信息加载数据常量-start********************/
	public final static int EDIT_LOADING_DATA=3001;//加载数据
	public final static int EDIT_ERROR1=3002;//编辑错误
	public final static int EDIT_SUCCESS=3003;//修改信息成功
	/********************修改用户信息加载数据常量-end********************/
	
	/********************登陆常量-start********************/
	public static boolean isLogin=false;
	
	/********************登陆常量-end**********************/
	
	
	/********************用户常量********************/
	 public final static String QQ="qq";
	 public final static String USER_ID="userId";
	 public final static String USER_ACCOUNT="userAccount";
	 public final static String USER_NAME="userName";
	 public final static String USER_EMAIL="userEmail";
	 public final static String USER_MOBILE_TELEPHONE="userMobileTelephone";
	 public final static String USER_OFFICE_TELEPHONE="userOfficeTelephone";
	 public final static String ROLE_ID="role_id";
	 public final static String UNIT_ID1="unit1_id";
	 public final static String UNIT_ID2="unit2_id";
	 public final static String UNIT_ID3="unit3_id";
	 public final static String UNIT_ID="unit_id";
	 public final static String UNIT_NAME="unit_name";
	 public final static String UNIT_NAME1="unit1_name";
	 public final static String UNIT_NAME2="unit2_name";
	 public final static String UNIT_NAME3="unit3_name";
	 public final static String S_USERNAME="s_userName";
	 public final static String S_USERPASSWORD="s_userPassword";
	/********************用户常量********************/
	 
	/********************密码修改常量********************/
	 public final static int EDIT_PASSWORD_SUCCESS=4001;
	 public final static int EDIT_PASSWORD_FAIL=4002;
	/********************密码修改常量********************/ 
	 
}
