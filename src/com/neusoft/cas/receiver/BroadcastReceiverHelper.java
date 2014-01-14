package com.neusoft.cas.receiver;

import com.neusoft.cas.util.ConstantUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.NetUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
 /**
   * 监听网络的广播
   * @ClassName: BroadcastReceiverHelper
   * @Description: TODO
   * @author yangchj
   * @date 2014-1-3 上午11:24:52
   */
public class BroadcastReceiverHelper extends BroadcastReceiver {

	private Context mContext;
	private BroadcastReceiverHelper receiver;
	private Handler mHandler;
	public BroadcastReceiverHelper(Context mContext, Handler mHandler) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		receiver=this;
	}

	/**
	  * @Title: 注册
	  * @Description: TODO
	  * @param @param action    设定文件
	  * @return void    返回类型
	  * @throws
	  */
	public void registerAction(String action) {
		IntentFilter filter=new IntentFilter();
		filter.addAction(action);
		mContext.registerReceiver(receiver, filter);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int type=NetUtils.getConnectedType(context);
		switch (type) {
		case -1:
			LogUtils.i("无网络连接");
			mHandler.sendEmptyMessage(ConstantUtils.NOT_HAS_NET);
			break;
		case ConnectivityManager.TYPE_WIFI:
			LogUtils.i("WIFI网络已连接");
			mHandler.sendEmptyMessage(ConstantUtils.HAS_WIFI);
			break;
		case ConnectivityManager.TYPE_MOBILE:
			LogUtils.i("手机网络已连接");
			mHandler.sendEmptyMessage(ConstantUtils.HAS_MOBILE);
			break;
		}
	}
}
