package com.neusoft.cas.widget;

import java.util.Date;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import net.simonvt.menudrawer.MenuDrawer;
import com.actionbarsherlock.view.Menu;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.ToastUtils;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView;
import com.ycj.android.widget.pulltorefresh.PullAndLoadListView.OnLoadMoreListener;
import com.ycj.android.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @ClassName: MainActivity
 * @Description: TODO
 * @author yangchj
 * @date 2013-12-31 ����2:49:50
 */
public class MainActivity extends BaseActivity {

	private MenuDrawer mDrawer;
	private PullAndLoadListView mListView = null;
	private SharedPreferencesUtils myPreference;
	private Context mContext;
	private MyTask mTask;
	private Map<String, String> map;
	private LinearLayout loading_Indicator;
	private long preTime;
	public static final long TWO_SECOND = 2 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		LogUtils.i(myPreference.getPrefString(ConstantUtils.SESSION_ID, ""));
	}

	/**
	 * 初始化
	 */
	protected void initView() {
		mDrawer = getmDrawer();
		mDrawer.setContentView(R.layout.layout_main);
		mListView = (PullAndLoadListView) findViewById(R.id.msg_list_item);
		loading_Indicator = (LinearLayout) findViewById(R.id.placeholder_loading);

		myPreference = SharedPreferencesUtils.getInstance(mContext);
		// 刷新
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshStatus();
			}
		});
		// 加载更多
		mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		// 读取上次更新时间
		// mListView.setLastUpdated(myPreference.getPrefString(ConstantUtils.LAST_UPDATE_TIME,
		// ""));
		// String
		// param="eap_username=admin&eap_password=1&eap_authentication=true";
		// new Thread(request).start();
		// mTask=new MyTask();
		// mTask.execute(param);

		// String jsonStr="{ user.name: cxhjhgjhg, sex: man }";
		// try {
		// org.json.JSONObject jsonObj=new JSONObject(jsonStr);
		// LogUtils.i(jsonObj.getString("user.name"));
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * @Title:刷新
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void refreshStatus() {

	}

	/**
	 * @Title: 加载更多
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void loadMoreData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	Runnable request = new Runnable() {
		@Override
		public void run() {
			LogUtils.i("dffffffffffffffffffff");
			String result = HttpUtils
					.sendGet(
							"http://10.4.120.103:8080/cas/techcomp/ria/commonProcessor!login.action",
							"eap_username=admin&eap_password=1&eap_authentication=true");
			if (TextUtils.isEmpty(result)) {
				LogUtils.i("返回结果为空");
			} else {
				LogUtils.i(result);
			}
			LogUtils.i("gggggggggggggggggggggg");
		}
	};

	/**
	 * @Title: 显示进度条
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showLoadingIndicator() {
		loading_Indicator.setVisibility(View.VISIBLE);
	}

	private void hideLoadIndicator() {
		loading_Indicator.setVisibility(View.GONE);
	}

	/**
	 * 后台请求
	 * 
	 * @ClassName: MyTask
	 * @Description: TODO
	 * @author yangchj
	 * @date 2013-12-31 下午3:36:38
	 */
	class MyTask extends AsyncTask<String, String, Map<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 显示等待进度条
			showLoadingIndicator();
		}

		@Override
		protected Map<String, Object> doInBackground(String... params) {
			String result = HttpUtils
					.sendGet(
							"http://10.4.120.103:8080/cas/techcomp/ria/commonProcessor!login.action",
							params[0]);
			LogUtils.i(result);
			return null;
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// 隐藏等待进度条
			hideLoadIndicator();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			// 隐藏等待进度条
			hideLoadIndicator();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 截获后退键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = new Date().getTime();
			// 如果时间间隔大于2秒, 不处理
			if ((currentTime - preTime) > TWO_SECOND) {
				// 显示消息
				ToastUtils.showToast(MainActivity.this, "再按一次退出.", Gravity.BOTTOM, 0, 40);
				// 更新时间
				preTime = currentTime;
				// 截获事件,不再处理
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}
}
