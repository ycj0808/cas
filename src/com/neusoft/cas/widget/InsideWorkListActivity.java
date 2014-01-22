package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.util.SharedPreferencesUtils;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

/**
 * 创建内部工单
 * 
 * @ClassName: InsideWorkListActivity
 * @Description: TODO
 * @author yangchj
 * @date 2014-2-2 下午3:51:48
 */
public class InsideWorkListActivity extends BaseMonitorActivity {

	private Context mContext;
	private SharedPreferencesUtils myPreference;
	private EditText edit_insideworklist_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_inside_worklist);
		initView();
		setOnClickListener();
	}

	/**
	 * @Title: 元素控件初始化
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
	}

	/**
	 * @Title: 设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {

	}

	/**
	 * @Title: 创建工单
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void createWorkList() {

	}
}
