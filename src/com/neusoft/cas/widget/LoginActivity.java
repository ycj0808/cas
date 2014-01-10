package com.neusoft.cas.widget;

import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 用户登陆
 * 
 * @ClassName: LoginActivity
 * @Description: TODO
 * @author yangchj
 * @date 2014-1-7 上午11:34:59
 */
public class LoginActivity extends BaseMonitorActivity {

	private TextView navBar_title;// 标题
	private Button btn_login;
	private Button btn_register;
	private CheckBox checkBox;
	private SharedPreferencesUtils myPreference;
	private Context mContext;
	private boolean rem_password = false;
	private String login_account = "";
	private String login_password = "";
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		initView();
		setOnClickListener();
	}

	/**
	 * @Title: 初始化控件元素
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		mContext = this;
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		rem_password = myPreference.getPrefBoolean(
				ConstantUtils.REMEMBER_PASSWORD, false);
		login_account = myPreference.getPrefString(ConstantUtils.LOGIN_ACCOUNT,"");
		login_password = myPreference.getPrefString(ConstantUtils.LOGIN_PASSWORD, "");
		navBar_title = (TextView) findViewById(R.id.navbar_title);
		navBar_title.setText(R.string.login_title);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		checkBox = (CheckBox) findViewById(R.id.rem_password);
		checkBox.setChecked(rem_password);
		if(!TextUtils.isEmpty(login_account)&&!TextUtils.isEmpty(login_password)){
			
		}
	}

	Runnable login_thread = new Runnable() {
		@Override
		public void run() {
			if(!TextUtils.isEmpty(login_account)||!TextUtils.isEmpty(login_password)){
				myHandler.sendEmptyMessage(ConstantUtils.LOGIN_ERROR1);
			}else{
				
			}
		}
	};

	/**
	 * @Title:设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		// 登陆监听事件
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//jumpToActivity(MainActivity.class);
				String encode_str=SecurityUtils.encryptBASE64("您好");
				LogUtils.i(encode_str);
				String decode_str=SecurityUtils.decryptBASE64(encode_str);
				LogUtils.i(decode_str);
				ToastUtils.showToast(LoginActivity.this, decode_str);
				jumpToActivity(MainActivity.class);
			}
		});
		// 注册监听事件
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToActivity(RegisterActivity.class);
			}
		});
		// 记住密码的CheckBox的变更监听事件
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				myPreference.setPrefBoolean(ConstantUtils.REMEMBER_PASSWORD,
						isChecked);
			}
		});
	}

	/**
	 * @Title: 跳转
	 * @Description: TODO
	 * @param @param cls 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void jumpToActivity(Class cls) {
		Intent intent = new Intent(LoginActivity.this, cls);
		startActivity(intent);
		finish();
	}

	/**
	 * @Title:忘记密码
	 * @Description: TODO
	 * @param @param v 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void forgotPwd(View v) {
		
	}
	
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ConstantUtils.LOGIN_ERROR1:
				
				break;
			default:
				break;
			}
		}
	};
}
