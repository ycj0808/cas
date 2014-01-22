package com.neusoft.cas.widget;

import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.DialogUtils;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RequirementsWorkListActivity extends BaseMonitorActivity {

	private Context mContext;
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
					//createWorkList();
					new Thread(new Runnable() {
						@Override
						public void run() {
							LogUtils.i("**********");
							Map<String,String> map=new HashMap<String, String>();
							map.put("name", "hh");
							String result=HttpUtils.sendPostRequestByJson("http://10.4.126.161:8080/ns/sum.do", map);
							LogUtils.i(result);
						}
					}).start();
					
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
		getParam(content,userId,userName,deptName,deptId,userPhone,userEmail);
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
}



















