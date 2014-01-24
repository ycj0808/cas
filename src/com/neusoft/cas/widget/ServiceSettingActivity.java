package com.neusoft.cas.widget;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.ui.utils.DialogUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

/**
 * 网络设置
 * 
 * @ClassName: ServiceSettingActivity
 * @Description: TODO
 * @author yangchj
 * @date 2014-1-23 下午2:36:56
 */
public class ServiceSettingActivity extends BaseMonitorActivity {

	private Context mContext;
	private EditText edit_service_addr;
	private String service_addr="";
	private SharedPreferencesUtils myPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		initView();
	}

	/**
	 * @Title:控件初始化
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		mContext = this;
		ActionBar actionBar = getSupportActionBar();
		// 设置是否显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		// 不显示Logo
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(R.string.lab_service_setting);
		edit_service_addr=(EditText) findViewById(R.id.edit_service_addr);
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		service_addr=myPreference.getPrefString(ConstantUtils.SERVICE_ADDR, ConstantUtils.COMMON_URL_SUFFIX);
		edit_service_addr.setText(service_addr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("保存").setIcon(R.drawable.ic_compose)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().equals("保存")){
			if(TextUtils.isEmpty(service_addr)){
				DialogUtils.showAlertDialog(mContext, "服务网址不能为空", "信息提示", "确认");
			}else{
				myPreference.setPrefString(ConstantUtils.SERVICE_ADDR, edit_service_addr.getText().toString());
				ToastUtils.showToast(ServiceSettingActivity.this, "保存成功");
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
