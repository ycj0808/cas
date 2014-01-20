package com.neusoft.cas.widget;


import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.receiver.BroadcastReceiverHelper;
import com.neusoft.cas.util.ConstantUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.ToastUtils;

public abstract class BaseActivity extends SherlockFragmentActivity {

	private MenuDrawer mDrawer;
	private LayoutInflater mInflater;
	private BroadcastReceiverHelper receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater=LayoutInflater.from(this);
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		//设置是否显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_home);
		// 添加MenuDrawer
		mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,
				Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
		mDrawer.setMenuView(R.layout.layout_menu);
		MenuFragment menu = (MenuFragment)getSupportFragmentManager().findFragmentById(R.id.left_menu);
		menu.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDrawer.closeMenu(); 
				switch (position) {
				case 1:
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								Bundle bundle=new Bundle();
								bundle.putString("typeId", "");
								jumpToActivity(InfoFirstActivity.class, bundle);
								finish();
							}
						}
					}).start();
					break;
				case 2:
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								Bundle bundle=new Bundle();
								bundle.putString("typeId", "1");
								jumpToActivity(InfoFirstActivity.class, bundle);
								finish();
							}
						}
					}).start();
					break;
				case 3:
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								Bundle bundle=new Bundle();
								bundle.putString("typeId", "2");
								jumpToActivity(InfoFirstActivity.class, bundle);
								finish();
							}
						}
					}).start();
					break;
				case 4:	
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								Bundle bundle=new Bundle();
								bundle.putString("typeId", "3");
								jumpToActivity(InfoFirstActivity.class, bundle);
								finish();
							}
						}
					}).start();
				    break;
				case 6:	
				    break; 
				case 8:
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								jumpToActivity(EditPersonInfoActivity.class,null);
							}
						}
					}).start();
					break;
				case 9:
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}finally{
								jumpToActivity(EditPasswordActivity.class,null);
							}
						}
					}).start();
					break;	
				case 11:
				    break;
				case 13:	
					Toast.makeText(getApplicationContext(), "退出",Toast.LENGTH_SHORT).show();
				    break;
				}
			}
		});
		//设置返回按钮的图标
//		mDrawer.setSlideDrawable(R.drawable.ic_back_holo_dark);
		//设置返回按钮，指示器是否可用
//		mDrawer.setDrawerIndicatorEnabled(false);
		mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
	}

	public MenuDrawer getmDrawer() {
		return mDrawer;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawer.toggleMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		// 注册广播接收器
		receiver = new BroadcastReceiverHelper(this,handler);
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
				Toast.makeText(getApplicationContext(), "无网络连接",Toast.LENGTH_SHORT).show();
				break;
			case ConstantUtils.HAS_WIFI:
				setHasWifi();//设置Wifi可用
				break;
			case ConstantUtils.HAS_MOBILE:
				setHasMobile();//设置手机网络可用
				break;
			}
		};
	};
	/**
	  * @Title:  设置网络不可用
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	private void setNotHasNetWork(){
		ConstantUtils.IS_MOBILE=false;
		ConstantUtils.IS_WIFI=false;
		logNetState();
	}
	/**
	  * @Title: 设置Wifi可用
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	private void setHasWifi(){
		ConstantUtils.IS_WIFI=true;
		ConstantUtils.IS_MOBILE=false;
		logNetState();
	}
	/**
	  * @Title: 设置手机网络可用
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	private void setHasMobile(){
		ConstantUtils.IS_WIFI=false;
		ConstantUtils.IS_MOBILE=true;
		logNetState();
	}
	/**
	  * @Title: 打印当前的
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	 */
	private void logNetState(){
		LogUtils.i(String.valueOf(ConstantUtils.IS_WIFI));
		LogUtils.i(String.valueOf(ConstantUtils.IS_MOBILE));
	}
	/**
	  * @Title: 跳转
	  * @Description: TODO
	  * @param @param cls    设定文件
	  * @return void    返回类型
	  * @throws
	 */
	protected void jumpToActivity(Class cls,Bundle bundle){
		Intent intent=new Intent(BaseActivity.this,cls);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
}
