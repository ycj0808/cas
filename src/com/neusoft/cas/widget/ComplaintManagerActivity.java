package com.neusoft.cas.widget;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.neusoft.cas.util.ConstantUtils;
import com.neusoft.cas.util.ImageUtils;
import com.neusoft.cas.util.SharedPreferencesUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.SecurityUtils;
import com.ycj.android.ui.utils.DialogUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ComplaintManagerActivity extends BaseMonitorActivity {

	private Context mContext;
	private SharedPreferencesUtils myPreference;
	private Dialog dialog;
	private EditText edit_card_num;// 问题卡数目
	private EditText edit_card_no;// 问题卡号
	private EditText edit_complaint_content;// 投诉内容
	private Button btn_worklist_type;
	private Button btn_complaint_submit;
	private String worklistType = "";
	private String card_num = "";
	private String card_no = "";
	private String complaint_content = "";
	private String userId = "";// 用户ID
	private String userName = "";// 用户名称
	private String deptId1 = "";
	private String deptId2 = "";
	private String deptId = "";// 部门ID
	private String userPhone = "";
	private String userTel = "";
	private String userEmail = "";
	private String login_pwd = "";
	private String login_name = "";
	private String service_url = "";
	private static final int SUCCESS = 101;
	private static final int FAIL = 102;
	private Map<String, String> paramMap = new HashMap<String, String>();
	private ImageView image_attachment;
	// 图片转化为Base64字符串
	private String base64Str = "";
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName = "";
	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_complaint_manager);
		initView();
		setOnClickListener();
	}

	/**
	 * @Title: 元素初始化
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressLint("NewApi")
	protected void initView() {
		mContext = this;
		// 获取actionBar,并设置相关特性
		ActionBar actionBar = getSupportActionBar();
		// 设置是否显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		// 不显示Logo
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(R.string.lab_create_worklist);
		myPreference = SharedPreferencesUtils.getInstance(mContext);
		edit_card_num = (EditText) findViewById(R.id.edit_card_num);
		edit_card_no = (EditText) findViewById(R.id.edit_card_no);
		edit_complaint_content = (EditText) findViewById(R.id.edit_complaint_content);
		btn_worklist_type = (Button) findViewById(R.id.btn_worklist_type);
		btn_complaint_submit = (Button) findViewById(R.id.btn_complaint_submit);
		service_url = myPreference.getPrefString(ConstantUtils.SERVICE_ADDR,
				ConstantUtils.STR_BASE_URL);

		// 初始化图片保存路径
		String photo_dir = ImageUtils.getFullImageDownPathDir();
		if (photo_dir.isEmpty() || photo_dir.length() == 0) {
			Toast.makeText(mContext, "存储卡不存在", Toast.LENGTH_LONG).show();
		} else {
			PHOTO_DIR = new File(photo_dir);
		}
		image_attachment = (ImageView) findViewById(R.id.img_attachment);
		paramMap.put("boId", "workOrder_complaintManager_complaintManagerBO_bo");
		paramMap.put("methodName", "doSaveAndStartPhone");
		paramMap.put("returnType", "json");
		new Thread(new Runnable() {
			@Override
			public void run() {
				userId = myPreference.getPrefString(ConstantUtils.USER_ID, "");
				userName = myPreference.getPrefString(ConstantUtils.USER_NAME,
						"");
				deptId1 = myPreference
						.getPrefString(ConstantUtils.UNIT_ID1, "");
				deptId2 = myPreference
						.getPrefString(ConstantUtils.UNIT_ID2, "");
				deptId = myPreference.getPrefString(ConstantUtils.UNIT_ID, "");
				userEmail = myPreference.getPrefString(
						ConstantUtils.USER_EMAIL, "");
				userPhone = myPreference.getPrefString(
						ConstantUtils.USER_MOBILE_TELEPHONE, "");
				userTel = myPreference.getPrefString(
						ConstantUtils.USER_OFFICE_TELEPHONE, "");
			}
		}).start();
	}

	/**
	 * @Title:设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		/**
		 * 选择工单类型对话框
		 */
		btn_worklist_type.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog().show();
			}
		});
		/**
		 * 提交菜单
		 */
		btn_complaint_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				card_num = edit_card_num.getText().toString();
				card_no = edit_card_no.getText().toString();
				complaint_content = edit_complaint_content.getText().toString();
				if (TextUtils.isEmpty(card_no)) {
					DialogUtils.showAlertDialog(mContext, "问题卡号不能为空", "信息提示",
							"确认");
				} else if (TextUtils.isEmpty(card_num)
						|| !TextUtils.isDigitsOnly(card_num)) {
					if (TextUtils.isEmpty(card_num)) {
						DialogUtils.showAlertDialog(mContext, "问题卡数目不能为空",
								"信息提示", "确认");
					} else {
						DialogUtils.showAlertDialog(mContext, "请填写数字", "信息提示",
								"确认");
					}
				} else if (TextUtils.isEmpty(complaint_content)) {
					DialogUtils.showAlertDialog(mContext, "投诉内容不能为空", "信息提示",
							"确认");
				} else if (TextUtils.isEmpty(worklistType)
						|| worklistType.equals(R.string.lab_worklist_type)) {
					DialogUtils.showAlertDialog(mContext, "请选择工单类型", "信息提示",
							"确认");
				} else {
					createWorkList();
				}
			}
		});

		// 拍照
		image_attachment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPickPhotoAction();
			}
		});
	}

	/**
	 * @Title: 创建工单
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void createWorkList() {
		showDialog("正在创建工单，请稍候...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				login_pwd = myPreference.getPrefString(
						ConstantUtils.S_USERPASSWORD, "");
				login_pwd = SecurityUtils.decryptBASE64(login_pwd);
				login_name = myPreference.getPrefString(
						ConstantUtils.S_USERNAME, "");
				paramMap.put(
						"parameters",
						getParam(card_num, card_no, complaint_content,
								worklistType));
				paramMap.put("eap_username", login_name);
				paramMap.put("eap_password", login_pwd);
				LogUtils.i(paramMap.toString());
				String result = HttpUtils.sendPostRequest(service_url
						+ ConstantUtils.COMMON_URL_SUFFIX, paramMap);
				Bundle bundle = new Bundle();
				Message msg = new Message();
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject obj = jsonObj.getJSONObject("response");
					if (obj != null && obj.getString("rtnCode").equals("00000")) {
						handler.sendEmptyMessage(SUCCESS);
					} else {
						msg.what = FAIL;
						bundle.putString("fail_info", "创建失败");
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					msg.what = FAIL;
					bundle.putString("fail_info", "网络访问,返回数据出错");
					msg.setData(bundle);
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = new Bundle();
			switch (msg.what) {
			case SUCCESS:
				closeDialog();
				ToastUtils.showToast(ComplaintManagerActivity.this, "创建工单成功");
				finish();
				break;
			case FAIL:
				bundle = msg.getData();
				closeDialog();
				ToastUtils.showToast(ComplaintManagerActivity.this,
						bundle.getString("fail_info"), Gravity.BOTTOM, 0, 40);
				break;
			}
		}
	};

	/**
	 * @Title: 获取参数
	 * @Description: TODO
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	protected String getParam(String card_num, String card_no, String content,
			String worklistType) {
		StringBuilder sb = new StringBuilder();
		sb.append("[{'String':'").append(userId).append("'},{'String':'")
				.append(deptId).append("'},");
		sb.append("{'String':'").append(deptId1).append("'},{'String':'")
				.append(deptId2).append("'},");
		sb.append("{'int':'").append(Integer.valueOf(card_num)).append("'},");
		sb.append("{'String':'").append(card_no).append("'},{'String':''},");
		sb.append("{'String':'").append(content).append("'},{'String':'")
				.append(userEmail).append("'},");
		sb.append("{'String':'").append(userPhone).append("'},{'String':'")
				.append(userTel).append("'},");
		sb.append("{'String':'phone'},{'String':'").append(worklistType)
				.append("'}");
		sb.append(",{'String':'").append(base64Str).append("'},{'String':'")
				.append(mFileName).append("'}]");
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(sb.toString());
		return m.replaceAll("");
	}

	/**
	 * @Title: 显示选择工单类型
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected Dialog createDialog() {
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.lab_worklist_type);
		final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
		builder.setSingleChoiceItems(R.array.worklisttype, 0, choiceListener);
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				int choiceWhich = choiceListener.getWhich();
				String worklist_type = getResources().getStringArray(
						R.array.worklisttype)[choiceWhich];
				worklistType = String.valueOf(choiceWhich + 1);
				LogUtils.i(worklistType);
				btn_worklist_type.setText(worklist_type);
			}
		};
		builder.setPositiveButton("确定", btnListener);
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 选择框的监听事件
	 * 
	 * @ClassName: ChoiceOnClickListener
	 * @Description: TODO
	 * @author yangchj
	 * @date 2014-2-2 下午2:05:26
	 */
	private class ChoiceOnClickListener implements
			DialogInterface.OnClickListener {
		private int which = 0;

		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			this.which = which;
		}

		public int getWhich() {
			return which;
		}
	}

	/**
	 * @Title: 显示对话框
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void showDialog(String msg) {
		View view = View.inflate(mContext, R.layout.layout_progress, null);
		TextView dialog_content = (TextView) view.findViewById(R.id.message);
		dialog_content.setText(msg);
		dialog = DialogUtils.showProgressBar(mContext, view);
	}

	/**
	 * @Title: 关闭对话框
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void closeDialog() {
		if (dialog != null & dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * @Title: 照相
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			Toast.makeText(mContext, "没有可用的存储卡", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			Log.i("TAG", "图片路径:" + mCurrentPhotoFile.getPath());
			Bitmap bitmap = ImageUtils.getBitmapFromSD(mCurrentPhotoFile,
					ImageUtils.SCALEIMG, 800, 80);
			image_attachment.setImageBitmap(bitmap);
			base64Str = ImageUtils.bitmapToBase64(bitmap);
			break;
		}
	}

	/**
	 * @Title:拍照
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			Toast.makeText(mContext, "未找到系统相机程序", Toast.LENGTH_LONG).show();
		}
	}
}
