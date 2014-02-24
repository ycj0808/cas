package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class InfoDetailActivity extends BaseMonitorActivity {

	private Context mContext;
	private WebView webView;
	private String infoUrl;
	private SharedPreferencesUtils myPreference;
	private String service_url = "";
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info_detail);
		initView();
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getBundleExtra("url");
			infoUrl=service_url+bundle.getString("infoUrl");
//			infoUrl = service_url + "techcomp/security/infoview.jsp?fpath="
//					+ bundle.getString("infoUrl") + "&title="
//					+ bundle.getString("title")+"&dType="+bundle.getString("type")
//					+"&time="+bundle.getString("time");
			webView.loadUrl(infoUrl);
		}
		// infoUrl = ConstantUtils.STR_BASE_URL
		// + "/upload/infomanage/055aed411ea3450a89a566bf057d15f6.jsp";

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
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebChromeClient(new WebViewClient());
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		service_url = myPreference.getPrefString(ConstantUtils.SERVICE_ADDR,
				ConstantUtils.STR_BASE_URL);
	}

	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

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
