package com.neusoft.cas.widget;


import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockFragmentActivity {

	private MenuDrawer mDrawer;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater=LayoutInflater.from(this);
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		// 添加MenuDrawer
		mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,
				Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
		mDrawer.setMenuView(R.layout.layout_menu);
		MenuFragment menu = (MenuFragment)getSupportFragmentManager().findFragmentById(R.id.left_menu);
		menu.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDrawer.setActiveView(view);
				mDrawer.closeMenu();    
			}
		});
		mDrawer.setSlideDrawable(R.drawable.ic_drawer);
		mDrawer.setDrawerIndicatorEnabled(true);
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
	
}
