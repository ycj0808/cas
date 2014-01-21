package com.neusoft.cas.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.adapter.InfoDownloadAdapter;
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
import com.ycj.android.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

import net.simonvt.menudrawer.MenuDrawer;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

public class FileDownloadActivity extends BaseActivity {
	private Context mContext;
	private MenuDrawer mDrawer;
	private SharedPreferencesUtils myPreference;
	private PullAndLoadListView listView;
	private InfoDownloadAdapter adapter;
	private LinearLayout loading_Indicator;
	private Map<String,String> paramMap=new HashMap<String, String>();
	private StringBuilder sb=new StringBuilder();
	private String pageNumber="1";
	private String pageSize="10";
	private String keyWord="";
	private String fileName="";
	private String login_pwd="";
	private PullToRefreshDataTask pullToRefresh;
	private LoadMoreDataTask loadMore;
	private int dataSize=0;
	private AjaxCallBack<File> callBack;//回调函数
	private long progress;
	private ProgressDialog m_pDialog;//进度条对话框
	private String storeDir="";
	private String filePath="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setOnClickListener();
	}
	/**
	  * @Title: 初始化元素控件
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	@SuppressWarnings("unchecked")
	protected void initView(){
		mContext=this;
		myPreference=SharedPreferencesUtils.getInstance(mContext);
		mDrawer=getmDrawer();
		mDrawer.setContentView(R.layout.layout_file_download);
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.lab_file_download);
		listView=(PullAndLoadListView) findViewById(R.id.list_info_download);
		adapter=new InfoDownloadAdapter(mContext);
		listView.setAdapter(adapter);
		loading_Indicator=(LinearLayout) findViewById(R.id.placeholder_download_loading);
		paramMap.put("boId", "infoManager_fileUpLoadBO_bo");
		paramMap.put("methodName", "getFileAllPhone");   
		paramMap.put("returnType", "json");
		paramMap.put("parameters",getParams(sb, fileName, keyWord, pageNumber, pageSize));
		paramMap.put("eap_username", myPreference.getPrefString(ConstantUtils.S_USERNAME, ""));
		login_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
		paramMap.put("eap_password",SecurityUtils.decryptBASE64(login_pwd));
		showLoading();
		pullToRefresh=new PullToRefreshDataTask();
		pullToRefresh.execute(paramMap);
		storeDir=CommonUtils.getStorePath();
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
				paramMap.put("parameters", getParams(sb, fileName,keyWord,pageNumber,pageSize));
				pullToRefresh=new PullToRefreshDataTask();
				pullToRefresh.execute(paramMap);
			}
		});
		//加载更多
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onLoadMore() {
				if(CasData.list_file_download.size()<dataSize){
					pageNumber=String.valueOf(Integer.valueOf(pageNumber)+1);
					paramMap.put("parameters", getParams(sb, fileName,keyWord,pageNumber,pageSize));
					loadMore=new LoadMoreDataTask();
					loadMore.execute(paramMap);
				}else{
					//通知数据变化了
					adapter.notifyDataSetChanged();
					//加载更多
					listView.onLoadMoreComplete();
					listView.setSelection(listView.selectId);
					LogUtils.i(String.valueOf(listView.selectId));
					ToastUtils.showToast(FileDownloadActivity.this, "数据已加载完成", Gravity.BOTTOM, 0, 40);
				}
			}
		});
		
		/**
		 * 点击事件
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				showProgressDialog();
				Map<String,Object> map=(Map<String, Object>) listView.getItemAtPosition(position);
				try {
					JSONObject obj=new JSONObject(map.get("csfilet").toString());
					String fileUrl=obj.getString("fileUrl");
				    downloadFile(fileUrl);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.showToast(FileDownloadActivity.this, "解析数据出错");
				}
			}
		});
		
		callBack=new AjaxCallBack<File>() {
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				LogUtils.i("progress = " + progress);
				if (current != count && current != 0) {
					progress = (int) (current / (float) count * 100);
				} else {
					progress = 100;
				}
				m_pDialog.setProgress((int)progress);
			}
			@Override
			public void onSuccess(File t) {
				super.onSuccess(t);
				LogUtils.i("下载完成...");
				m_pDialog.dismiss();
				ToastUtils.showToast(FileDownloadActivity.this, "文件已保存于 : "+filePath, Gravity.BOTTOM, 0, 40);
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtils.i("下载失败..."+strMsg, t);
				m_pDialog.dismiss();
				ToastUtils.showToast(FileDownloadActivity.this, "文件下载失败", Gravity.BOTTOM, 0, 40);
			}
			@Override
			public void onStart() {
				super.onStart();
				LogUtils.i("开始下载");
			}
		};
	}
	
	 /**
	   * 下拉刷新
	   * @ClassName: PullToRefreshDataTask
	   * @Description: TODO
	   * @author yangchj
	   * @date 2014-1-21 下午3:11:40
	  */
	private class PullToRefreshDataTask extends AsyncTask<Map<String,String>, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtils.i("后台运行,马上开始");
		}
		
		@Override
		protected Void doInBackground(Map<String, String>... params) {
			if (isCancelled()) {
				return null;
			}
			String result=HttpUtils.sendPostRequest(ConstantUtils.STR_COMMON_URL, params[0]);
			LogUtils.i(result);
			if(!TextUtils.isEmpty(result)){
				try {
					JSONObject jsonObject=new JSONObject(result);
					JSONObject obj=jsonObject.getJSONObject("response");
					CasData.list_file_download.clear();
					CasData.list_file_download=JsonUtils.getListMaps(obj,"infos");
					dataSize=Integer.valueOf(obj.getString("info_count"));
					LogUtils.i(String.valueOf(CasData.list_file_download.size()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			closeLoading();
			//通知数据变化了
			adapter.notifyDataSetChanged();
			//下拉刷新完成
			listView.onRefreshComplete(DateUtils.getNowTime(DateUtils.DEFAULT_DATETIME_FORMAT));
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//通知刷新完成
			listView.onRefreshComplete();
			closeLoading();
		}
	}
	
	private class LoadMoreDataTask extends AsyncTask<Map<String,String>, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtils.i("后台运行,马上开始");
		}
		@Override
		protected Void doInBackground(Map<String, String>... params) {
			if (isCancelled()) {
				return null;
			}
			
			String url=ConstantUtils.STR_COMMON_URL;
			String result=HttpUtils.sendPostRequest(url, params[0]);
			LogUtils.i(result);
			if(!TextUtils.isEmpty(result)){
				try {
					List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
					JSONObject jsonObject=new JSONObject(result);
					JSONObject obj=jsonObject.getJSONObject("response");
					list=JsonUtils.getListMaps(obj,"infos");
					dataSize=Integer.valueOf(obj.getString("info_count"));
					for(int i=0;i<list.size();i++){
						CasData.list_file_download.add((Map<String, Object>) list.get(i));
					}
					LogUtils.i(String.valueOf(CasData.list_file_download.size()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//通知数据变化了
			adapter.notifyDataSetChanged();
			//加载更多
			listView.onLoadMoreComplete();
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//通知刷新完成
			listView.onLoadMoreComplete();
		}
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
	/**
	  * @Title:生成参数 
	  * @param @param sb
	  * @param @param fileName
	  * @param @param keyWord
	  * @param @param pageNumber
	  * @param @param pageSize
	  * @param @return    设定文件
	  * @return String    返回类型
	  * @throws
	 */
	public String getParams(StringBuilder sb,String fileName,String keyWord,String pageNumber,String pageSize){
		sb.delete( 0, sb.length());
		sb.append("[{'String':'").append(myPreference.getPrefString(ConstantUtils.ROLE_ID, "")).append("'},");
		sb.append("{'String':'2'},").append("{'String':'").append(fileName).append("'},");
		sb.append("{'String':'").append(keyWord).append("'},").append("{'String':'").append(pageNumber).append("'},").append("{'String':'").append(pageSize).append("'}]");
		return sb.toString();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		login_pwd=myPreference.getPrefString(ConstantUtils.S_USERPASSWORD, "");
		paramMap.put("eap_password",SecurityUtils.decryptBASE64(login_pwd));
		storeDir=CommonUtils.getStorePath();
	}
	
	
	/**
	  * @Title:下载文件 
	  * @Description: TODO
	  * @param @param fileName    设定文件
	  * @return void    返回类型
	  * @throws
	 */
	protected void downloadFile(String fileUrl){
		if(TextUtils.isEmpty(storeDir)){
			ToastUtils.showToast(FileDownloadActivity.this, "请检查是否已插入内存卡");
		}else{
			FinalHttp fh = new FinalHttp();  
			fileUrl=ConstantUtils.STR_BASE_URL+fileUrl;
			filePath=storeDir+fileUrl.subSequence(fileUrl.lastIndexOf("/"), fileUrl.length());
			LogUtils.i(filePath);
			fh.download(fileUrl, filePath,callBack);
		}
	}
	/**
	 * 显示进度条对话框
	 */
	protected void showProgressDialog(){
		m_pDialog = new ProgressDialog(FileDownloadActivity.this);
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		m_pDialog.setMessage("正在下载文件，请稍候...");
		m_pDialog.setProgress(100);
		m_pDialog.setIndeterminate(false);
		m_pDialog.setCancelable(false);
		m_pDialog.show();
	}
}
