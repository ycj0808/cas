package com.neusoft.cas.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.adapter.InfoFirstAdapter;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.JsonUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView.OnLoadMoreListener;
import com.ycj.android.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

public class InfoFirstActivity extends BaseActivity {

	private List<Map<String, Object>> list_info;// 数据信息
	private Context mContext;
	private PullAndLoadListView listView;
	private InfoFirstAdapter adapter;
	private MenuDrawer mDrawer;
	private String jsessionid;
	private SharedPreferencesUtils myPreference;
	private Map<String,String> paramMap=new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setOnClickListener();
	}
	 /**
	  * @Title: 初始化控件
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	@SuppressWarnings("unchecked")
	protected void initView() {
		mContext=this;
		myPreference=SharedPreferencesUtils.getInstance(mContext);
		mDrawer=getmDrawer();
		mDrawer.setContentView(R.layout.layout_info_first);
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
//		// 设置是否显示返回按钮
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		// 不显示Logo
//		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(R.string.lab_info_first);
		listView=(PullAndLoadListView) findViewById(R.id.list_info_first);
		adapter=new InfoFirstAdapter(mContext, list_info);
		listView.setAdapter(adapter);
		jsessionid=myPreference.getPrefString(ConstantUtils.SESSION_ID, "");
		paramMap.put("boId", "infoManager_infoHomePageBO_bo");
		paramMap.put("methodName", "getCsInfoPhone");
		paramMap.put("returnType", "json");
		paramMap.put("parameters", "[{String:'1'}]");
		//paramMap.put("jsessionid", jsessionid);
		paramMap.put("eap_username", "admin");
		paramMap.put("eap_password", "666666");
		new PullToRefreshDataTask().execute(paramMap);
	}
	 /**
	  * @Title: 设置监听事件
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void setOnClickListener(){
		//下拉刷新的监听事件
		listView.setOnRefreshListener(new OnRefreshListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onRefresh() {
				new PullToRefreshDataTask().execute(paramMap);
			}
		});
		//加载更多的监听事件
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				new LoadMoreDataTask().execute();
			}
		});
	}

	 /**
	   * 下拉刷新
	   * @ClassName: PullToRefreshDataTask
	   * @Description: TODO
	   * @author yangchj
	   * @date 2014-1-13 下午1:30:31
	   */
	private class PullToRefreshDataTask extends AsyncTask<Map<String,String>, Void, Void>{

		//后台运行前的操作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		//后台运行
		@Override
		protected Void doInBackground(Map<String,String>... params) {
			String url=ConstantUtils.STR_COMMON_URL;
			String result=HttpUtils.sendPostRequest(url, params[0]);
			List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
			LogUtils.i(result);
			if(!TextUtils.isEmpty(result)){
				try {
					JSONObject jsonObject=new JSONObject(result);
					tmp=JsonUtils.getListMaps(jsonObject);
					list_info=tmp;
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		//后台程序运行结束后的操作
		@Override
		protected void onPostExecute(Void result) {
			//通知数据变化了
			adapter.notifyDataSetChanged();
			//下拉刷新完成
			listView.onRefreshComplete();
			super.onPostExecute(result);
		}
		/**
		 * 取消下拉刷新操作
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//通知刷新完成
			listView.onRefreshComplete();
		}
	}
	
	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void>{

		//后台运行前的操作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		//后台运行
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		//后台程序运行结束后的操作
		@Override
		protected void onPostExecute(Void result) {
			//通知数据变化了
			adapter.notifyDataSetChanged();
			//加载更多
			listView.onLoadMoreComplete();
			super.onPostExecute(result);
		}
		/**
		 * 取消下拉刷新操作
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//通知刷新完成
			listView.onLoadMoreComplete();
		}
	}
}
