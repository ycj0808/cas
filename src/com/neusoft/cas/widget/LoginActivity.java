package com.neusoft.cas.widget;

import com.neusoft.cas.receiver.BroadcastReceiverHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
  /**
   * 用户登陆
   * @ClassName: LoginActivity
   * @Description: TODO
   * @author yangchj
   * @date 2014-1-7 上午11:34:59
   */
public class LoginActivity extends BaseMonitorActivity {
	
	private TextView navBar_title;//标题
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
	  * @param          设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void initView(){
		navBar_title=(TextView) findViewById(R.id.navbar_title);
		navBar_title.setText(R.string.login_title);
	}
	  /**
	   * @Title:设置监听事件 
	   * @Description: TODO
	   * @param     设定文件
	   * @return void    返回类型
	   * @throws
	   */
	protected void setOnClickListener(){
		
	}
}
