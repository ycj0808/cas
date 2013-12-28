package com.neusoft.cas.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockListFragment;
import com.neusoft.cas.adapter.MenuAdapter;

public class MenuFragment extends SherlockListFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		List<Object> listItems=new ArrayList<Object>();
		listItems.add(new Item("我的信用卡", R.drawable.ic_action_refresh_dark));
//		listItems.add(new Category("我的账户"));
//		listItems.add(new Category("账单查询"));
//		listItems.add(new Category("快速还款"));
//		listItems.add(new Category("我的消息"));
		listItems.add(new Item("我的办卡", R.drawable.ic_action_select_all_dark));
//		listItems.add(new Category("我要办卡"));
//		listItems.add(new Category("办卡进度"));
//		listItems.add(new Category("寄送查询"));
//		listItems.add(new Category("我要开卡"));
		listItems.add(new Item("设置", R.drawable.ic_action_refresh_dark));
//		listItems.add(new Category("设置"));
//		listItems.add(new Category("反馈"));
//		listItems.add(new Category("关于"));
		listItems.add(new Item("我的信用卡", R.drawable.ic_action_refresh_dark));
		listItems.add(new Item("我的办卡", R.drawable.ic_action_select_all_dark));
		listItems.add(new Item("设置", R.drawable.ic_action_refresh_dark));
		
		MenuAdapter menuAdapter=new MenuAdapter(getActivity(), listItems);
		setListAdapter(menuAdapter);
	}
}
