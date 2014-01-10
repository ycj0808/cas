package com.neusoft.cas.widget;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ForgotPasswordActivity extends BaseMonitorActivity {

	private Button btn_find_pwd;
	private EditText edit_find_pwd_account;
	private ImageView navbar_imageView_left;
	private TextView txt_navbar_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_find_pwd);
		initView();
		setClickListener();
	}
	
	/**
	  * @Title: 初始化控件
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void initView(){
		btn_find_pwd=(Button) findViewById(R.id.btn_find_pwd);
		edit_find_pwd_account=(EditText) findViewById(R.id.edit_find_pwd_account);
		navbar_imageView_left=(ImageView) findViewById(R.id.navbar_imageView_left);
		navbar_imageView_left.setVisibility(View.VISIBLE);
		txt_navbar_title=(TextView) findViewById(R.id.navbar_title);
		txt_navbar_title.setText(R.string.lab_find_pwd);
		txt_navbar_title.setVisibility(View.VISIBLE);
	}
	/**
	  * @Title: 设置监听事件
	  * @Description: TODO
	  * @param          设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void setClickListener(){
		navbar_imageView_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
