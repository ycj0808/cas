package com.neusoft.cas.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import net.simonvt.menudrawer.MenuDrawer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
//import com.actionbarsherlock.view.MenuItem;
import com.neusoft.cas.adapter.InfoFirstAdapter;
import com.neusoft.cas.domain.CasData;
import com.neusoft.cas.util.CommonUtils;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.DateUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.JsonUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.ToastUtils;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView.OnLoadMoreListener;
//import com.ycj.android.widget.pulltorefresh.PullAndLoadListView.OnLoadMoreListener;
import com.ycj.android.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
 /**
   * 信息首页
   * @ClassName: InfoFirstActivity
   * @Description: TODO
   * @author yangchj
   * @date 2014-1-22 下午3:52:28
   */
public class InfoFirstActivity extends BaseActivity implements OnNavigationListener {

	private Context mContext;
	private PullAndLoadListView listView;
	private InfoFirstAdapter adapter;
	private MenuDrawer mDrawer;
	private String jsessionid;
	private SharedPreferencesUtils myPreference;
	private Map<String,String> paramMap=new HashMap<String, String>();
	private String [] infotypes;
	private String typeId="";
	private StringBuilder sb=new StringBuilder();	
	private LinearLayout loading_Indicator;
	private String pageNumber="1";
	private String pageSize="15";
	private int dataSize=0; 
	private PullToRefreshDataTask pullToRefresh;
	private LoadMoreDataTask loadMore;
	private String login_pwd="";
	private String service_url="";
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
		loading_Indicator = (LinearLayout) findViewById(R.id.placeholder_loading);
		listView=(PullAndLoadListView) findViewById(R.id.list_info_first);
		adapter=new InfoFirstAdapter(mContext);
		listView.setAdapter(adapter);

		showLoading();
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			typeId=bundle.getString("typeId");
			if("1".equals(typeId)){
				actionBar.setTitle(R.string.lab_busi_info);
			}else if("2".equals(typeId)){
				actionBar.setTitle(R.string.lab_quality_report);
			}else if("3".equals(typeId)){
				actionBar.setTitle(R.string.lab_common_fault);
			}else{
				infotypes= getResources().getStringArray(R.array.infotype);
				Context context =actionBar.getThemedContext();
				ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context,R.array.infotype,R.layout.sherlock_spinner_item);
				list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				actionBar.setListNavigationCallbacks(list, this);
			}
		}else{
			actionBar.setTitle(R.string.lab_info_first);
			infotypes= getResources().getStringArray(R.array.infotype);
			Context context =actionBar.getThemedContext();
			ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context,R.array.infotype,R.layout.sherlock_spinner_item);
			list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(list, this);
		}
		jsessionid=myPreference.getPrefString(ConstantUtils.SESSION_ID, "");
		paramMap.put("boId", "infoManager_infoReleaseBO_bo");
		paramMap.put("methodName", "getInfo2Phone");   
		paramMap.put("returnType", "json");
		paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
//		paramMap.put("jsessionid", jsessionid);
		paramMap.put("eap_username", myPreference.getPrefString(ConstantUtils.S_USERNAME, ""));
		login_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
		service_url=myPreference.getPrefString(ConstantUtils.SERVICE_ADDR, ConstantUtils.STR_BASE_URL);
		paramMap.put("eap_password",login_pwd);
		pullToRefresh=new PullToRefreshDataTask();
		pullToRefresh.execute(paramMap);
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
				pageNumber="1";
				paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
				pullToRefresh=new PullToRefreshDataTask();
				pullToRefresh.execute(paramMap);
			}
		});
		//加载更多的监听事件
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onLoadMore() {
				if(CasData.list_info.size()<dataSize){
					pageNumber=String.valueOf(Integer.valueOf(pageNumber)+1);
					paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
					loadMore=new LoadMoreDataTask();
					loadMore.execute(paramMap);
				}else{
					//通知数据变化了
					adapter.notifyDataSetChanged();
					//加载更多
					listView.onLoadMoreComplete();
					listView.setSelection(listView.selectId);
					LogUtils.i(String.valueOf(listView.selectId));
					ToastUtils.showToast(InfoFirstActivity.this, "数据已加载完成", Gravity.BOTTOM, 0, 40);
				}
			}
		});
		//单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Map<String,Object> map=(Map<String, Object>) listView.getItemAtPosition(position);
				try {
					JSONObject obj=new JSONObject(map.get("csinfot").toString());
					Bundle bundle=new Bundle();
					bundle.putString("infoUrl", obj.getString("infoUrl"));
					bundle.putString("title", obj.getString("infoTitle"));
					bundle.putString("time", DateUtils.getDateFromLongTime(Long.valueOf(obj.getLong("publishTime"))));
					bundle.putString("type", obj.getString("infoType"));
					Intent intent=new Intent(InfoFirstActivity.this,InfoDetailActivity.class);
					intent.putExtra("url", bundle);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.showToast(InfoFirstActivity.this, "解析数据出错");
				}
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
			LogUtils.i("**********************************************");
		}
		
		//后台运行
		@Override
		protected Void doInBackground(Map<String,String>... params) {
			if (isCancelled()) {
				return null;
			}
			String result=HttpUtils.sendPostRequest(service_url+ConstantUtils.COMMON_URL_SUFFIX, params[0]);
//			List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
			LogUtils.i(result);
			if(!TextUtils.isEmpty(result)){
				try {
					JSONObject jsonObject=new JSONObject(result);
					JSONObject obj=jsonObject.getJSONObject("response");
					CasData.list_info.clear();
					CasData.list_info=JsonUtils.getListMaps(obj,"infos");
					dataSize=Integer.valueOf(obj.getString("info_count"));
					LogUtils.i(String.valueOf(CasData.list_info.size()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		//后台程序运行结束后的操作
		@Override
		protected void onPostExecute(Void result) {
			closeLoading();
			//通知数据变化了
			adapter.notifyDataSetChanged();
			//下拉刷新完成
			listView.onRefreshComplete(DateUtils.getNowTime(DateUtils.DEFAULT_DATETIME_FORMAT));
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
			closeLoading();
		}
	}
	
	private class LoadMoreDataTask extends AsyncTask<Map<String,String>, Void, Void>{

		//后台运行前的操作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtils.i("**********************************************");
		}
		//后台运行
		@Override
		protected Void doInBackground(Map<String,String>... params) {
			if (isCancelled()) {
				return null;
			}
			String result=HttpUtils.sendPostRequest(service_url+ConstantUtils.COMMON_URL_SUFFIX, params[0]);
			LogUtils.i(result);
			if(!TextUtils.isEmpty(result)){
				try {
					List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
					JSONObject jsonObject=new JSONObject(result);
					JSONObject obj=jsonObject.getJSONObject("response");
					list=JsonUtils.getListMaps(obj,"infos");
					dataSize=Integer.valueOf(obj.getString("info_count"));
					for(int i=0;i<list.size();i++){
						CasData.list_info.add((Map<String, Object>) list.get(i));
					}
					LogUtils.i(String.valueOf(CasData.list_info.size()));
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition) {
		case 0:
			typeId="";
			pageNumber="1";
			paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
			showLoading();
			pullToRefresh=new PullToRefreshDataTask();
			pullToRefresh.execute(paramMap);
			LogUtils.i(typeId);
			break;
		case 1:
			typeId="1";
			pageNumber="1";
			paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
			showLoading();
			pullToRefresh=new PullToRefreshDataTask();
			pullToRefresh.execute(paramMap);
			LogUtils.i(typeId);
			break;
		case 2:
			typeId="2";
			pageNumber="1";
			paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
			showLoading();
			pullToRefresh=new PullToRefreshDataTask();
			pullToRefresh.execute(paramMap);
			LogUtils.i(typeId);
			break;
		case 3:
			typeId="3";
			pageNumber="1";
			paramMap.put("parameters", getParams(sb, typeId,pageNumber,pageSize));
			showLoading();
			pullToRefresh=new PullToRefreshDataTask();
			pullToRefresh.execute(paramMap);
			LogUtils.i(typeId);
			break;
		}
		return false;
	}
	
	
	
	/**
	  * @Title: 生成参数
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	 */
	public String getParams(StringBuilder sb,String typeId,String pageNumber,String pageSize){
		sb.delete( 0, sb.length() );
		sb.append("[{'String':'").append(myPreference.getPrefString(ConstantUtils.ROLE_ID, "")).append("'},");
		sb.append("{'String':'2'},").append("{'String':'").append(typeId).append("'},");
		sb.append("{'String':''},").append("{'String':'").append(pageNumber).append("'},").append("{'String':'").append(pageSize).append("'}]");
		return sb.toString();
	}
	
	/**
	  * @Title: 显示进度条
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	public void showLoading(){
		listView.setVisibility(View.GONE);
		loading_Indicator.setVisibility(View.VISIBLE);
	}
	/**
	  * @Title: 关闭进度条
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	public void closeLoading(){
		listView.setVisibility(View.VISIBLE);
		loading_Indicator.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		login_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
		paramMap.put("eap_password",login_pwd);
	}
}
