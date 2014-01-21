package com.neusoft.cas.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 公共工具类
 * 
 * @ClassName: CommonUtils
 * @Description: TODO
 * @author yangchj
 * @date 2014-1-3 下午4:22:33
 */
public class CommonUtils {
	/**
	 * @Title:获取手机的信息 mBrand 手机品牌,mType 手机型号,mImei 设备号,mImsi ,mNumber
	 *                手机号,mSserviceName 运营商
	 * @Description: TODO
	 * @param @param mContext
	 * @param @return 设定文件
	 * @return Map 返回类型
	 * @throws
	 */
	public static Map<String, String> getPhoneInfo(Context mContext) {
		Map<String, String> map = new HashMap<String, String>();
		TelephonyManager telphony = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		map.put("mBrand", android.os.Build.BRAND);
		map.put("mType", android.os.Build.MODEL);
		map.put("mImei", telphony.getDeviceId());
		map.put("mImsi", telphony.getSubscriberId());
		map.put("mNumber", telphony.getLine1Number());
		map.put("mSserviceName", telphony.getSimOperatorName());
		return map;
	}

	/**
	 * @Title: 获取应用的版本信息
	 * @Description: TODO
	 * @param @param mContext
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static Map<String, Object> getVersionCode(Context mContext) {
		Map<String, Object> map = new HashMap<String, Object>();
		int versionCode = 1;
		String versionName = "1.0";
		try {
			versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
			versionName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		map.put("versionCode", versionCode);
		map.put("versionName", versionName);
		return map;
	}
	/**
	 * 判断是否插入内存卡
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 获取文件保存路径
	 * @return
	 */
	public static String getStorePath(){
		if(isHasSdcard()){
			String path=Environment.getExternalStorageDirectory().getPath()+"/cas";
			File f=new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			return path;
		}else{
			return "";
		}
	}
}
