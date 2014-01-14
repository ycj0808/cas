package com.neusoft.cas.widget;

import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	
	private boolean firstBoot = true;
	private Context mContext;
	private SharedPreferencesUtils myPreference;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.default_wallpaper);
	    iv.setLayoutParams(new LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
		setContentView(iv);
		mContext=this;
		myPreference=SharedPreferencesUtils.getInstance(mContext);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					//判断是否是第一次使用此应用
					firstBoot=myPreference.getPrefBoolean(ConstantUtils.FIRST_LOGIN, true);
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (firstBoot) {
						myPreference.setPrefBoolean(ConstantUtils.FIRST_LOGIN, false);
						System.out.println(myPreference.getPrefBoolean(ConstantUtils.FIRST_LOGIN, true));
						//第一次使用
						JumpToActivity(GuideActivity.class);
						
					}else{
						JumpToActivity(LoginActivity.class);
					}
				}
				
			}
		}).start();
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
		finish();
	}
}
