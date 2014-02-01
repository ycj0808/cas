package com.neusoft.cas.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.adapter.InfoFirstAdapter;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.RegexUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.DialogUtils;
import com.ycj.android.ui.utils.ToastUtils;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView.OnLoadMoreListener;
import com.ycj.android.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.AsyncTask;
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

public class EditPersonInfoActivity extends BaseMonitorActivity {

	private Context mContext;
	private Button btn_edit;
	private Dialog dialog;
	private SharedPreferencesUtils myPreference;
	private String userName = "";// 用户姓名
	private String userEmail = "";// 用户邮箱
	private String userId = "";// 用户ID
	private String userPhone = "";// 用户手机
	private String officePhone = "";// 办公电话
	private String userQq = "";// 用户QQ
	private EditText edit_userName;
	private EditText edit_userEmail;
	private EditText edit_userPhone;
	private EditText edit_officePhone;
	private EditText edit_userQq;
	private boolean isEmail=true;
	private boolean isPhone=true;
	private boolean isTel=true;
	private boolean isNameNull=false;
	private String login_pwd="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_person_info);
		mContext = this;
		initView();
		setOnClickListener();
	}

	/**
	 * @Title: 初始化控件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		// 设置是否显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		// 不显示Logo
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(R.string.lab_edit_person_info);
		btn_edit = (Button) findViewById(R.id.btn_edit_person_info);
		edit_userName = (EditText) findViewById(R.id.edit_person_info_name);
		edit_userEmail = (EditText) findViewById(R.id.edit_person_info_email);
		edit_userPhone = (EditText) findViewById(R.id.edit_person_info_phone);
		edit_officePhone = (EditText) findViewById(R.id.edit_person_info_office_phone);
		edit_userQq = (EditText) findViewById(R.id.edit_person_info_qq);
		showDialog(getResources().getString(R.string.lab_loading_data));
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		loadUserInfo();
	}

	/**
	 * @Title: 设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		// 确认修改
		btn_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog("修改信息中,请稍候...");
				sendEditUserInfo();
			}
		});
		//邮箱编辑框监听事件
		edit_userEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				isEmail=RegexUtils.checkEmail(s.toString());
				userEmail=s.toString();
			}
		});
		//用户姓名的监听事件
		edit_userName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s.toString())){
					isNameNull=true;//用户名为空
				}else{
					isNameNull=false;
				}
				userName=s.toString();
			}
		});
		//办公电话的监听事件
		edit_officePhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(s)){
					isTel=RegexUtils.checkPhone(s.toString());
				}else{
					isTel=true;
				}
				officePhone=s.toString();
			}
		});
		//手机的监听事件
		edit_userPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				isPhone=RegexUtils.checkMobile(s.toString());
				userPhone=s.toString();
			}
		});
		//qq监听事件
		edit_userQq.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				userQq=s.toString();
			}
		});
	}

	/**
	 * @Title: 修改用户信息
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void sendEditUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg=new Message();
				Bundle bundle=new Bundle();
				msg.what=ConstantUtils.EDIT_ERROR1;
				if(isNameNull){
					bundle.putString("fail_info", "用户姓名不能为空");
					msg.setData(bundle);
					handler.sendMessage(msg);
				}else if(!isEmail){
					bundle.putString("fail_info", "邮箱格式不正确");
					msg.setData(bundle);
					handler.sendMessage(msg);
				}else if(!isPhone){
					bundle.putString("fail_info", "手机格式不正确");
					msg.setData(bundle);
					handler.sendMessage(msg);
				}else if(!isTel){
					bundle.putString("fail_info", "办公电话格式不正确");
					msg.setData(bundle);
					handler.sendMessage(msg);
				}else{
					Map<String,String> map=new HashMap<String, String>();
					map.put("boId", "common_CommonUserBO_bo");
					map.put("methodName", "updateUserInfo");
					map.put("returnType", "Json");
					StringBuilder sb=new StringBuilder();
					sb.append("[{String:'").append(userId).append("'},{String:'").append(userName);
					sb.append("'},{String:'").append(userPhone).append("'},{String:'").append(officePhone);
					sb.append("'},{String:'").append(userEmail).append("'},{String:'").append(userQq).append("'}]");
					LogUtils.i(sb.toString());
					map.put("parameters", sb.toString());
//					paramMap.put("jsessionid", jsessionid);
					map.put("eap_username", myPreference.getPrefString(ConstantUtils.S_USERNAME, ""));
					login_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
					map.put("eap_password",SecurityUtils.decryptBASE64(login_pwd));
					String result=HttpUtils.sendPostRequest(ConstantUtils.STR_COMMON_URL, map);
					if(TextUtils.isEmpty(result)){
						myPreference.setPrefString(ConstantUtils.USER_NAME, userName);
						myPreference.setPrefString(ConstantUtils.USER_EMAIL, userEmail);
						myPreference.setPrefString(ConstantUtils.USER_MOBILE_TELEPHONE, userPhone);
						myPreference.setPrefString(ConstantUtils.USER_OFFICE_TELEPHONE, officePhone);
						myPreference.setPrefString(ConstantUtils.QQ, userQq);
						handler.sendEmptyMessage(ConstantUtils.EDIT_SUCCESS);
					}else{
						bundle.putString("fail_info", result);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				}
			}   
		}).start();
	}

	/**
	 * @Title:加载用户信息
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void loadUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				userId = myPreference.getPrefString(ConstantUtils.USER_ID, "");
				userName = myPreference.getPrefString(ConstantUtils.USER_NAME,"");
				userPhone = myPreference.getPrefString(
						ConstantUtils.USER_MOBILE_TELEPHONE, "");
				officePhone = myPreference.getPrefString(
						ConstantUtils.USER_OFFICE_TELEPHONE, "");
				userEmail = myPreference.getPrefString(
						ConstantUtils.USER_EMAIL, "");
				userQq = myPreference.getPrefString(ConstantUtils.QQ, "");
				edit_userName.setText(userName);
				edit_userEmail.setText(userEmail);
				edit_officePhone.setText(officePhone);
				edit_userPhone.setText(userPhone);
				edit_userQq.setText(userQq);
				handler.sendEmptyMessage(ConstantUtils.EDIT_LOADING_DATA);
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
	 * @Title: 关闭对话框
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void closeDialog() {
		if (dialog != null & dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * @Title: 显示对话框
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void showDialog(String msg) {
		View view = View.inflate(mContext, R.layout.layout_progress, null);
		TextView dialog_content = (TextView) view.findViewById(R.id.message);
		dialog_content.setText(msg);
		dialog = DialogUtils.showProgressBar(mContext, view);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ConstantUtils.EDIT_LOADING_DATA:
					closeDialog();
				break;
			case ConstantUtils.EDIT_SUCCESS:
				closeDialog();
				ToastUtils.showToast(EditPersonInfoActivity.this, "信息修改成功",Gravity.BOTTOM, 0, 40);
				finish();
				break;
			case ConstantUtils.EDIT_ERROR1:
				Bundle bundle=msg.getData();
				closeDialog();
				AlertDialog.Builder builder = new Builder(EditPersonInfoActivity.this); 
				builder.setTitle("提示");
				builder.setPositiveButton("确定",null); 
				builder.setIcon(android.R.drawable.ic_dialog_info); 
				builder.setMessage(bundle.getString("fail_info")); 
				builder.show();
				break;
			}
		};
	};
}
