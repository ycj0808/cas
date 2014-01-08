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
		listItems.add(new Item("信息中心", 0));
		listItems.add(new Category("信息首页"));
		listItems.add(new Category("业务信息"));
		listItems.add(new Category("质量通报"));
		listItems.add(new Category("常见故障"));
		listItems.add(new Item("发起工单", 0));
		listItems.add(new Category("发起工单"));
		listItems.add(new Item("个人中心", 0));
		listItems.add(new Category("修改个人资料"));
		listItems.add(new Category("修改登录密码"));
		listItems.add(new Item("下载中心", 0));
		listItems.add(new Category("下载中心"));
		listItems.add(new Item("设置", 0));
		listItems.add(new Category("退出账号"));
		MenuAdapter menuAdapter=new MenuAdapter(getActivity(), listItems);
		setListAdapter(menuAdapter);
	}
}
