package com.neusoft.cas.widget;

import java.util.HashMap;
import java.util.Map;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditPasswordActivity extends BaseMonitorActivity {

	private Context mContext;
	private EditText edit_old_pwd;
	private EditText edit_new_pwd;
	private EditText edit_new_pwd1;
	private SharedPreferencesUtils myPreference;
	private String old_pwd="";
	private String dec_old_pwd="";
	private String new_pwd="";
	private String new_pwd1="";
	private boolean same=false;
	private boolean simple=true;//密码过于简单
	private boolean isOld=false;
	private Button btn_edit_password;
	private Map<String,String> paramMap=new HashMap<String, String>();
	private StringBuilder sb=new StringBuilder();
	private Dialog dialog;
	private View view;
	private TextView dialog_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_password);
		initView();
		setOnClickListener();
	}

   /**
	 * @Title:初始化元素控件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		mContext = this;
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		// 不显示Logo
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.lab_edit_password);
		edit_old_pwd=(EditText) findViewById(R.id.edit_old_password);
		edit_new_pwd=(EditText) findViewById(R.id.edit_new_password);
		edit_new_pwd1=(EditText) findViewById(R.id.edit_new_password1);
		btn_edit_password=(Button) findViewById(R.id.btn_edit_password);
		myPreference=SharedPreferencesUtils.getInstance(mContext);
		old_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
		paramMap.put("boId", "common_CommonUserBO_bo");
		paramMap.put("methodName", "updateUserPwd");
		paramMap.put("returnType", "Json");
		paramMap.put("eap_username", myPreference.getPrefString(ConstantUtils.S_USERNAME, ""));
		paramMap.put("eap_password",SecurityUtils.decryptBASE64(old_pwd));
		view = View.inflate(mContext, R.layout.layout_progress, null);
		dialog_content = (TextView) view.findViewById(R.id.message);
		dialog_content.setText(R.string.lab_logining);
		dialog = new AlertDialog.Builder(mContext).create();
		dialog.setCanceledOnTouchOutside(false);
	}
	/**
	  * @Title: 设置监听事件
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void setOnClickListener(){
		//原密码编辑框监听事件
		edit_old_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(SecurityUtils.decryptBASE64(old_pwd).equals(s.toString())){
					isOld=true;
					dec_old_pwd=SecurityUtils.decryptBASE64(old_pwd);
				}else{
					isOld=false;
				}
			}
	});
		//新密码编辑框
		edit_new_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()<6){
					simple=true;
				}else{
					simple=false;
					new_pwd=s.toString();
				}
			}
		});
		//新密码确认框
		edit_new_pwd1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(s)&s.toString().equals(edit_new_pwd.getText().toString())){
					same=true;
					new_pwd1=s.toString();
				}else{
					same=false;
				}
			}
		});
		//提交
		btn_edit_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isOld){
					if(TextUtils.isEmpty(edit_old_pwd.getText())){
						showAlertDialog("原密码不能为空");
					}else{
						showAlertDialog("原密码输入错误,请重新输入");
					}
				}else if(simple){
					if(TextUtils.isEmpty(edit_new_pwd.getText())){
						showAlertDialog("新密码不能为空");
					}else{
						showAlertDialog("新密码过于简单");
					}
				}else if(!same){
					showAlertDialog("新密码两次输入不一致");
				}else{
					editPwd();
				}
			}
		});
	}
	/**
	  * @Title: 编辑密码
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void editPwd(){
		if (dialog != null & !dialog.isShowing()) {
			dialog.show();
			dialog.setContentView(view);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg=new Message();
				Bundle bundle=new Bundle();
				sb.append("[{String:'").append(myPreference.getPrefString(ConstantUtils.USER_ID, ""));
				sb.append("'},{String:'").append(dec_old_pwd).append("'},{String:'");
				sb.append(new_pwd).append("'}]");
				paramMap.put("parameters", sb.toString());
//				paramMap.put("jsessionid", "");
				LogUtils.i(paramMap.toString());
				String result=HttpUtils.sendPostRequest(ConstantUtils.STR_COMMON_URL, paramMap);
				if(result.equals("success")){
					myPreference.setPrefString(ConstantUtils.S_USERPASSWORD, SecurityUtils.encryptBASE64(new_pwd));
					myPreference.setPrefBoolean(ConstantUtils.REMEMBER_PASSWORD, false);
					myPreference.removeShare(ConstantUtils.LOGIN_PASSWORD);
					msg.what=ConstantUtils.EDIT_PASSWORD_SUCCESS;
					bundle.putString("fail_info", "密码修改成功");
					msg.setData(bundle);
					myHandler.sendMessage(msg);
				}else{
					msg.what=ConstantUtils.EDIT_PASSWORD_FAIL;
					bundle.putString("fail_info", result);
					msg.setData(bundle);
					myHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	  * @Title:显示提示对话框 
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	 */
	protected void showAlertDialog(String content){
		AlertDialog.Builder builder = new Builder(EditPasswordActivity.this); 
		builder.setTitle("提示");
		builder.setPositiveButton("确定",null); 
		builder.setIcon(android.R.drawable.ic_dialog_info); 
		builder.setMessage(content);
		builder.show();
	}
	/**
	 * 消息处理
	 */
	private Handler myHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			Bundle bundle=new Bundle();
			switch (msg.what) {
			case ConstantUtils.EDIT_PASSWORD_SUCCESS:
				bundle=msg.getData();
				closeDialog();
//				showAlertDialog(bundle.getString("fail_info"));
				ToastUtils.showToast(EditPasswordActivity.this, bundle.getString("fail_info"), Gravity.BOTTOM, 0, 40);
				finish();
				break;
			case ConstantUtils.EDIT_PASSWORD_FAIL:
				bundle=msg.getData();
				closeDialog();
//				showAlertDialog(bundle.getString("fail_info"));
				ToastUtils.showToast(EditPasswordActivity.this, bundle.getString("fail_info"), Gravity.BOTTOM, 0, 40);
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
