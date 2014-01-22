package com.neusoft.cas.widget;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.receiver.BroadcastReceiverHelper;
import com.neusoft.cas.util.ConstantUtils;
import com.ycj.android.common.utils.LogUtils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 基础类,用于监听网络
 * 
 * @ClassName: BaseMonitorActivity
 * @Description: TODO
 * @author yangchj
 * @date 2014-1-7 上午11:40:17
 */
public class BaseMonitorActivity extends SherlockFragmentActivity {

	private BroadcastReceiverHelper receiver;//监听广播
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		// 注册广播接收器
		receiver = new BroadcastReceiverHelper(this, handler);
		receiver.registerAction(ConnectivityManager.CONNECTIVITY_ACTION);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 取消广播接收器
		unregisterReceiver(receiver);
		super.onStop();
	}

	/**
	 * 处理网络是否连接
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ConstantUtils.NOT_HAS_NET:
				setNotHasNetWork();
				Toast.makeText(getApplicationContext(), "无网络连接",
						Toast.LENGTH_SHORT).show();
				break;
			case ConstantUtils.HAS_WIFI:
				setHasWifi();// 设置Wifi可用
				break;
			case ConstantUtils.HAS_MOBILE:
				setHasMobile();// 设置手机网络可用
				break;
			}
		};
	};

	/**
	 * @Title: 设置网络不可用
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setNotHasNetWork() {
		ConstantUtils.IS_MOBILE = false;
		ConstantUtils.IS_WIFI = false;
		logNetState();
	}

	/**
	 * @Title: 设置Wifi可用
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setHasWifi() {
		ConstantUtils.IS_WIFI = true;
		ConstantUtils.IS_MOBILE = false;
		logNetState();
	}

	/**
	 * @Title: 设置手机网络可用
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setHasMobile() {
		ConstantUtils.IS_WIFI = false;
		ConstantUtils.IS_MOBILE = true;
		logNetState();
	}

	/**
	 * @Title: 打印当前的
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void logNetState() {
		LogUtils.i(String.valueOf(ConstantUtils.IS_WIFI));
		LogUtils.i(String.valueOf(ConstantUtils.IS_MOBILE));
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
