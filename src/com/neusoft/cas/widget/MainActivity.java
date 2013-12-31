package com.neusoft.cas.widget;

import net.simonvt.menudrawer.MenuDrawer;

import com.actionbarsherlock.view.Menu;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

	private MenuDrawer mDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawer=getmDrawer();
		mDrawer.setContentView(R.layout.layout_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
