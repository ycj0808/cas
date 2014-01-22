package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.DialogUtils;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RequirementsWorkListActivity extends BaseMonitorActivity {

	private Context mContext;
	private Dialog dialog;
	private SharedPreferencesUtils myPreference;
	private EditText edit_requirements_content;
	private Button btn_submit;
	private String content="";//工单内容
	private String userId="";//用户ID
	private String userName="";//用户名称
	private String deptName="";//部门名称
	private String deptId="";//部门ID
	private String userPhone="";
	private String userEmail="";
	private static final int SUCCESS=101;
	private static final int FAIL=102;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_requirements_worklist);
		initView();
		setOnClickListener();
	}

	/**
	 * @Title: 元素初始化
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		mContext = this;
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		// 设置是否显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		// 不显示Logo
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(R.string.lab_requirement_worklist);
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		edit_requirements_content = (EditText) findViewById(R.id.edit_requirements_content);
		btn_submit=(Button) findViewById(R.id.btn_requirements_submit);
		new Thread(new Runnable() {
			@Override
			public void run() {
				userId=myPreference.getPrefString(ConstantUtils.USER_ID, "");
				userName=myPreference.getPrefString(ConstantUtils.USER_NAME, "");
				userEmail=myPreference.getPrefString(ConstantUtils.USER_EMAIL, "");
				userPhone=myPreference.getPrefString(ConstantUtils.USER_MOBILE_TELEPHONE, "");
				deptId=myPreference.getPrefString(ConstantUtils.UNIT_ID3, "");
				deptName=myPreference.getPrefString(ConstantUtils.UNIT_NAME3, "");
				LogUtils.i("userId: "+userId+"    "+"userName: "+userName);
				LogUtils.i("userEmail: "+userEmail+"    "+"userPhone: "+userPhone);
				LogUtils.i("userPhone: "+userPhone+"    "+"deptId: "+deptId+" "+"deptName: "+deptName);
			}
		}).start();
	}

	/**
	 * @Title: 设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		//提交按钮
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				content=edit_requirements_content.getText().toString();
				if(TextUtils.isEmpty(content)){
					DialogUtils.showAlertDialog(mContext, "需求工单内容不能为空", "信息提示", "确认");
				}else{
					createWorkList();
				}
			}
		});
	}

	/**
	 * @Title: 创建工单
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void createWorkList() {
		//getParam(content,userId,userName,deptName,deptId,userPhone,userEmail);
		showDialog("正在创建工单，请稍候...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				
			}
		}).start();
	}
	/**
	  * @Title: 
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	 */
	protected String getParam(String content,String userId,String userName,String deptName,
			String deptId,String phone,String email){
		StringBuilder sb=new StringBuilder();
		sb.append("[{'String':'").append(content).append("'},{'String':'").append(userId).append("'},{'String':'");
		sb.append(userName).append("'},{'String':'").append(deptName).append("'},{'String':'").append(deptId);
		sb.append("'},{'String':'").append(userPhone).append("'},{'String':'").append(userEmail).append("'},{'String':''}]");
		LogUtils.i(sb.toString());
		return sb.toString();
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
	
	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				closeDialog();
				break;
			case FAIL:
				closeDialog();
				break;
			}
		}
	};
}



















