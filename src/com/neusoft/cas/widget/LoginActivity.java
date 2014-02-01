package com.neusoft.cas.widget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import net.simonvt.menudrawer.MenuDrawer;

import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
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
	private TextView txt_forgot_password;
	private SharedPreferencesUtils myPreference;
	private Context mContext;
	private boolean rem_password = false;
	private String login_account = "";
	private String login_password = "";
	private Dialog dialog;
	private TextView dialog_content;
	private View view;
	private EditText edit_login_account;
	private EditText edit_login_password;
	private boolean stop = true;// 登陆开关,if true,则正常,if false,则停止

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		initView();
		setOnClickListener();
		autoLogin();
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
		login_account = myPreference.getPrefString(ConstantUtils.LOGIN_ACCOUNT,
				"");
		login_password = myPreference.getPrefString(
				ConstantUtils.LOGIN_PASSWORD, "");
		navBar_title = (TextView) findViewById(R.id.navbar_title);
		navBar_title.setText(R.string.login_title);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		checkBox = (CheckBox) findViewById(R.id.rem_password);
		checkBox.setChecked(rem_password);
		edit_login_account = (EditText) findViewById(R.id.edit_login_account);
		edit_login_password = (EditText) findViewById(R.id.edit_login_password);
		txt_forgot_password = (TextView) findViewById(R.id.txt_forgot_password);
		view = View.inflate(mContext, R.layout.layout_progress, null);
		dialog_content = (TextView) view.findViewById(R.id.message);
		dialog_content.setText(R.string.lab_logining);
		dialog = new AlertDialog.Builder(mContext).create();
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * @Title: 设置自动登陆
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void autoLogin() {
		if (!TextUtils.isEmpty(login_account)
				&& !TextUtils.isEmpty(login_password)) {
			login_password = SecurityUtils.decryptBASE64(login_password);
			LogUtils.i(login_password);
			edit_login_account.setText(login_account);
			edit_login_password.setText(login_password);
			if (dialog != null & !dialog.isShowing()) {
				dialog.show();
				dialog.setContentView(view);
			}
			new Thread(login_thread).start();
		}

	}

	Runnable login_thread = new Runnable() {
		@Override
		public void run() {
			login_account = edit_login_account.getText().toString();
			login_password = edit_login_password.getText().toString();
			if (TextUtils.isEmpty(login_account)
					|| TextUtils.isEmpty(login_password)) {
				myHandler.sendEmptyMessage(ConstantUtils.LOGIN_ERROR1);
			} else {
				// try {
				// Thread.sleep(3000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				String url = ConstantUtils.STR_LOGIN_URL;
				String params = "eap_username=" + login_account
						+ "&eap_password=" + login_password
						+ "&eap_authentication=true";
				Map<String,Object> map= HttpUtils.sendLoginGet(url, params);
				String result=map.get("result").toString();
				if (!TextUtils.isEmpty(result)&&result.equals("success")) {
					if (stop) {
						myPreference.setPrefString(ConstantUtils.SESSION_ID,map.get("jsessionid").toString());
						Map<String,String> paramMap=new HashMap<String, String>();
						paramMap.put("boId", "common_CommonUserBO_bo");
						paramMap.put("methodName", "getUserDetailByUserAccountPhone");
						paramMap.put("returnType", "json");
						StringBuilder sb=new StringBuilder();
						sb.append("[{String:'").append(login_account).append("'}]");
						paramMap.put("parameters", sb.toString());
						paramMap.put("jsessionid", map.get("jsessionid").toString());
						paramMap.put("eap_username", login_account);
						paramMap.put("eap_password", login_password);
						result=HttpUtils.sendPostRequest(ConstantUtils.STR_COMMON_URL, paramMap);
						LogUtils.i(result);
						StringBuilder sbStr=new StringBuilder();
						sbStr.append("boId=common_CommonUserBO_bo&methodName=getUserDetailByUserAccountPhone");
						sbStr.append("&returnType=json").append("&parameters=").append(sb.toString()).append("&jsessionid=").append(map.get("jsessionid").toString());
//						result=HttpUtils.sendPost(url, sbStr.toString());
//						result=HttpUtils.sendGet(ConstantUtils.STR_COMMON_URL, sbStr.toString());
						try {
							JSONObject obj=new JSONObject(result);
							JSONObject jsonObject=obj.getJSONObject("response");
							if(jsonObject!=null){
								myPreference.setPrefString(ConstantUtils.QQ, jsonObject.getString("qq"));
								myPreference.setPrefString(ConstantUtils.USER_ID, jsonObject.getString("userId"));
								myPreference.setPrefString(ConstantUtils.USER_ACCOUNT, jsonObject.getString("userAccount"));
								myPreference.setPrefString(ConstantUtils.USER_NAME, jsonObject.getString("userName"));
								myPreference.setPrefString(ConstantUtils.USER_EMAIL, jsonObject.getString("userEmail"));
								myPreference.setPrefString(ConstantUtils.USER_MOBILE_TELEPHONE, jsonObject.getString("userMobileTelephone"));
								myPreference.setPrefString(ConstantUtils.USER_OFFICE_TELEPHONE, jsonObject.getString("userOfficeTelephone"));
								myPreference.setPrefString(ConstantUtils.ROLE_ID, jsonObject.getString("role_id"));
								myPreference.setPrefString(ConstantUtils.UNIT_ID1, jsonObject.getString("unit1_id"));
								myPreference.setPrefString(ConstantUtils.UNIT_ID2, jsonObject.getString("unit2_id"));
								myPreference.setPrefString(ConstantUtils.UNIT_ID3, jsonObject.getString("unit3_id"));								
								myPreference.setPrefString(ConstantUtils.S_USERNAME,login_account);
								myPreference.setPrefString(ConstantUtils.S_USERPASSWORD,SecurityUtils.encryptBASE64(login_password));
								myHandler.sendEmptyMessage(ConstantUtils.LOGIN_SUCCESS); 
							}else{
								myHandler.sendEmptyMessage(ConstantUtils.LOGIN_ERROR3);
							}
						} catch (JSONException e) {
							myHandler.sendEmptyMessage(ConstantUtils.LOGIN_ERROR3);
							e.printStackTrace();
						}
					} else {
						stop = true;
					}
				} else {
					Message msg=new Message();
					msg.what=ConstantUtils.LOGIN_ERROR2;
					Bundle bundle=new Bundle();
					bundle.putString("fail_info", map.get("fail_info").toString());
					msg.setData(bundle);
					myHandler.sendMessage(msg);
				}
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
				if (dialog != null & !dialog.isShowing()) {
					dialog.show();
					dialog.setContentView(view);
				}
				new Thread(login_thread).start();
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
				if (!isChecked) {
					myPreference.removeShare(ConstantUtils.LOGIN_PASSWORD);
					LogUtils.i("remove password");
				}
			}
		});
		// 找回密码的监听事件
		txt_forgot_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtils.i("找回密码");
				jumpToActivity(ForgotPasswordActivity.class);
			}
		});
		// dialog运行时,设置监听按键事件
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					LogUtils.i("设置stop开关");
					stop = false;
				}
				return false;
			}
		});
		// 登陆账号
		edit_login_account.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
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
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Bundle bundle=new Bundle();
			switch (msg.what) {
			case ConstantUtils.LOGIN_ERROR1:
				closeDialog();
				ToastUtils.showToast(LoginActivity.this,
						R.string.lab_login_error1, Gravity.BOTTOM, 0, 40);
				break;
			case ConstantUtils.LOGIN_ERROR2:
				bundle=msg.getData();
				closeDialog();
				ToastUtils.showToast(LoginActivity.this,bundle.getString("fail_info"), Gravity.BOTTOM, 0, 40);
				break;
			case ConstantUtils.LOGIN_ERROR3:
				closeDialog();
				ToastUtils.showToast(LoginActivity.this,"解析数据出错", Gravity.BOTTOM, 0, 40);
				break;
			case ConstantUtils.LOGIN_SUCCESS:
				if (checkBox.isChecked()) {
					myPreference.setPrefString(ConstantUtils.LOGIN_ACCOUNT,
							edit_login_account.getText().toString());
					myPreference.setPrefString(ConstantUtils.LOGIN_PASSWORD,
							SecurityUtils.encryptBASE64(edit_login_password
									.getText().toString()));
				}
				ConstantUtils.isLogin=true;
				ToastUtils.showToast(LoginActivity.this,
						R.string.lab_login_success, Gravity.BOTTOM, 0, 40);
				closeDialog();
				jumpToActivity(InfoFirstActivity.class);
				finish();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * @Title: 关闭diaglog
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void closeDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
