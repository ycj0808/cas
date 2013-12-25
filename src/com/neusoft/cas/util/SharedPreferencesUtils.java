package com.neusoft.cas.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: SharedPreferencesUtils
 * @Description: TODO(手机存储简单信息的工具类)
 *               使用说明：在程序中调用SharedPreferencesUtils.getInstance(context)+方法名即可
 * @author yangchj
 * @date 2013-5-16 上午11:25:22
 */
public class SharedPreferencesUtils {

	private static SharedPreferencesUtils myPreference;

	private String packageName = "";
	private static SharedPreferences sharedPreference = null;

	public static SharedPreferences getSharedPreference() {
		return sharedPreference;
	}

	public SharedPreferencesUtils(Context context) {
		packageName = "settings";
		sharedPreference = context.getSharedPreferences(packageName,
				Context.MODE_PRIVATE);
	}

	public static synchronized SharedPreferencesUtils getInstance(
			Context context) {
		if (myPreference == null)
			myPreference = new SharedPreferencesUtils(context);
		return myPreference;
	}

	/**
	 * 读取字符型的数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getPrefString(String key, final String defaultValue) {
		return sharedPreference.getString(key, defaultValue);
	}

	/**
	 * 设置字符型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setPrefString(final String key, final String value) {
		sharedPreference.edit().putString(key, value);
	}

	/**
	 * 获取boolean的数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getPrefBoolean(final String key, final boolean defaultValue) {
		return sharedPreference.getBoolean(key, defaultValue);
	}

	/**
	 * 获取是否包含键值
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(final String key) {
		return sharedPreference.contains(key);
	}

	/**
	 * 设置boolean类型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setPrefBoolean(final String key, final boolean value) {
		sharedPreference.edit().putBoolean(key, value);
	}

	/**
	 * 设置整型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setPrefInt(final String key, final int value) {
		sharedPreference.edit().putInt(key, value).commit();
	}

	/**
	 * 读取整型数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getPrefInt(final String key, final int defaultValue) {
		return sharedPreference.getInt(key, defaultValue);
	}

	/**
	 * 设置浮点型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setPrefFloat(final String key, final float value) {
		sharedPreference.edit().putFloat(key, value).commit();
	}

	/**
	 * 读取浮点型数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getPrefFloat(final String key, final float defaultValue) {
		return sharedPreference.getFloat(key, defaultValue);
	}

	/**
	 * 设置长整型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setSettingLong(final String key, final long value) {
		sharedPreference.edit().putLong(key, value).commit();
	}

	/**
	 * 读取长整型数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getPrefLong(final String key, final long defaultValue) {
		return sharedPreference.getLong(key, defaultValue);
	}

	/**
	 * 清空数据
	 */
	public void clearPreference() {
		final Editor editor = sharedPreference.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 移除指定的键值对
	 * 
	 * @param key
	 */
	public void removeShare(final String key) {
		sharedPreference.edit().remove(key);
	}
}
