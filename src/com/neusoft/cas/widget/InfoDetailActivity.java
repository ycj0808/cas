package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.util.ConstantUtils;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

public class InfoDetailActivity extends BaseMonitorActivity {

	private Context mContext;
	private WebView webView;
	private String infoUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info_detail);
		initView();
		 if(savedInstanceState!=null){
			 infoUrl=ConstantUtils.STR_BASE_URL+savedInstanceState.getString("infoUrl");
		 }
//		infoUrl = ConstantUtils.STR_BASE_URL
//				+ "/upload/infomanage/055aed411ea3450a89a566bf057d15f6.jsp";
		webView.loadUrl(infoUrl);
	}

	/**
	 * @Title: 初始化控件
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
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(R.string.lab_info_detail);
		webView = (WebView) findViewById(R.id.webView1);
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
}
