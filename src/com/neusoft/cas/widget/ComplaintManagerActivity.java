package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.DialogUtils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ComplaintManagerActivity extends BaseMonitorActivity {

	private Context mContext;
	private SharedPreferencesUtils myPreference;
	private EditText edit_card_num;// 问题卡数目
	private EditText edit_card_no;// 问题卡号
	private EditText edit_complaint_content;// 投诉内容
	private Button btn_worklist_type;
	private Button btn_complaint_submit;
	private String worklistType = "0";
	private String card_num="";
	private String card_no="";
	private String complaint_content="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_complaint_manager);
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
		actionBar.setTitle(R.string.lab_create_worklist);
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		edit_card_num = (EditText) findViewById(R.id.edit_card_num);
		edit_card_no = (EditText) findViewById(R.id.edit_card_no);
		edit_complaint_content = (EditText) findViewById(R.id.edit_complaint_content);
		btn_worklist_type = (Button) findViewById(R.id.btn_worklist_type);
		btn_complaint_submit=(Button) findViewById(R.id.btn_complaint_submit);
	}

	/**
	 * @Title:设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		/**
		 * 选择工单类型对话框
		 */
		btn_worklist_type.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog().show();
			}
		});
		/**
		 * 提交菜单
		 */
		btn_complaint_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				card_num=edit_card_no.getText().toString();
				card_no=edit_card_num.getText().toString();
				complaint_content=edit_complaint_content.getText().toString();
				if(TextUtils.isEmpty(card_num)){
					DialogUtils.showAlertDialog(mContext, "问题卡号不能为空", "信息提示", "确认");
				}else if(TextUtils.isEmpty(card_no)){
					DialogUtils.showAlertDialog(mContext, "问题卡数目不能为空", "信息提示", "确认");
				}else if(TextUtils.isEmpty(complaint_content)){
					DialogUtils.showAlertDialog(mContext, "投诉内容不能为空", "信息提示", "确认");
				}else{
					
				}
			}
		});
	}
	/**
	  * @Title: 创建工单
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void createWorkList(){
		
	}
	/**
	 * @Title: 显示选择工单类型
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected Dialog createDialog() {
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.lab_worklist_type);
		final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
		builder.setSingleChoiceItems(R.array.worklisttype, 0, choiceListener);
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				int choiceWhich = choiceListener.getWhich();
				String worklist_type = getResources().getStringArray(
						R.array.worklisttype)[choiceWhich];
				worklistType = String.valueOf(choiceWhich + 1);
				LogUtils.i(worklistType);
				btn_worklist_type.setText(worklist_type);
			}
		};
		builder.setPositiveButton("确定", btnListener);
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 选择框的监听事件
	 * 
	 * @ClassName: ChoiceOnClickListener
	 * @Description: TODO
	 * @author yangchj
	 * @date 2014-2-2 下午2:05:26
	 */
	private class ChoiceOnClickListener implements
			DialogInterface.OnClickListener {
		private int which = 0;

		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			this.which = which;
		}

		public int getWhich() {
			return which;
		}
	}
	
	
}
