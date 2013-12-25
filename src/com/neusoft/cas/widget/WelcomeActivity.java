package com.neusoft.cas.widget;

import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
	
	private boolean firstBoot = true;
	private Context mContext;
	private SharedPreferencesUtils myPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		myPreference=SharedPreferencesUtils.getInstance(mContext);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					//判断是否是第一次使用此应用
					firstBoot=myPreference.getPrefBoolean(ConstantUtils.FirstLogin, true);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (firstBoot) {
						//第一次使用
						
					}else{
						JumpToActivity(MainActivity.class);
					}
				}
				
			}
		});
	}
	
	/**
	 * @Title: JumpToActivity
	 * @Description: TODO(Activity跳转方法)
	 * @param @param cls  设定文件
	 * @return void       返回类型
	 * @throws
	 */
	private void JumpToActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}
}
