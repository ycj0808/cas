package com.neusoft.cas.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.neusoft.cas.domain.Role;
import com.neusoft.cas.domain.Unit;
import com.neusoft.cas.util.ConstantUtils;
import com.ycj.android.common.utils.CommonUtils;
import com.ycj.android.common.utils.HttpUtils;
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.common.utils.RegexUtils;
import com.ycj.android.ui.utils.DialogUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends BaseMonitorActivity {

	private EditText edit_email;// 邮箱
	private EditText edit_name;// 姓名
	private EditText edit_phone;// 手机
	private EditText edit_office_phone;// 办公电话
	private EditText edit_qq;// QQ号
	private Button btn_register;// 按钮
	private TextView navBar_title;
	private Spinner unit1_spinner;
	private Spinner unit2_spinner;
	private Spinner unit3_spinner;
	private Spinner role_spinner;
	private String unit1_id = "0";// 一级部门ID
	private String unit2_id = "0";// 二级部门ID
	private String unit3_id = "0";// 三级部门ID
	private String role_id = "0";// 角色ID
	private String userName="";//姓名
	private String userEmail="";//邮箱
	private String userPhone="";//手机
	private String userTel="";//办公电话
	private String userQq="";
	private ArrayAdapter<Unit> adapter01;
	private ArrayAdapter<Unit> adapter02;
	private ArrayAdapter<Unit> adapter03;
	private ArrayAdapter<Role> adapter04;
	private Map<String, List<Unit>> map = new HashMap<String, List<Unit>>();
	private List<Role> role_list = new ArrayList<Role>();
	private List<Unit> unit1_list = new ArrayList<Unit>();
	private Dialog dialog;
	private Dialog rDialog;
	private Context mContext;
	private String json_str = "";
	private boolean isEmail = false;// 判断是isEmail
	private boolean isMobilePhone = false;
	private boolean isTel = false;
	private RegisterThread registerThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		initView();
		setOnClickListener();
		View view = View.inflate(mContext, R.layout.layout_progress, null);
		TextView dialog_content = (TextView) view.findViewById(R.id.message);
		dialog_content.setText(R.string.lab_loading_data);
		dialog = DialogUtils.showProgressBar(mContext, view);
		// loadUnitSpinner();
		loadUnitData();
	}

	/**
	 * @Title:初始化控件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initView() {
		mContext = this;
		edit_email = (EditText) findViewById(R.id.edit_register_email);
		edit_name = (EditText) findViewById(R.id.edit_register_name);
		edit_phone = (EditText) findViewById(R.id.edit_register_phone);
		edit_office_phone = (EditText) findViewById(R.id.edit_register_office_phone);
		edit_qq = (EditText) findViewById(R.id.edit_register_qq);
		btn_register = (Button) findViewById(R.id.btn_submit);
		navBar_title = (TextView) findViewById(R.id.navbar_title);
		navBar_title.setText(R.string.register_title);
		unit1_spinner = (Spinner) findViewById(R.id.mySpinner01);
		unit2_spinner = (Spinner) findViewById(R.id.mySpinner02);
		unit3_spinner = (Spinner) findViewById(R.id.mySpinner03);
		role_spinner = (Spinner) findViewById(R.id.mySpinner04);
	}

	/**
	 * @Title:设置监听事件
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setOnClickListener() {
		// 注册的监听事件
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registerThread=new RegisterThread();
				registerThread.start();
				View view = View.inflate(mContext, R.layout.layout_progress, null);
				TextView dialog_content = (TextView) view.findViewById(R.id.message);
				dialog_content.setText(R.string.lab_registering);
				rDialog = DialogUtils.showProgressBar(mContext, view);
			}
		});
		// 一级部门spinner监听事件
		unit1_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,
					int i, long l) {
				unit1_id = ((Unit) unit1_spinner.getSelectedItem())
						.getUnit_id();
				LogUtils.i("一级部门:" + unit1_id);
				// List<Unit> unit2_list=map.get(unit1_id);
				// if(unit2_list==null){
				// unit2_list=new ArrayList<Unit>();
				// unit2_list.add(new Unit("0","请选择"));
				// }
				List<Unit> unit2_list = getListUnitByParentUnitId(unit1_id);
				adapter02 = new ArrayAdapter<Unit>(RegisterActivity.this,
						android.R.layout.simple_spinner_item, unit2_list);
				adapter02
						.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				unit2_spinner.setAdapter(adapter02);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		// 二级部门spinner监听事件
		unit2_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,
					int i, long l) {
				unit2_id = ((Unit) unit2_spinner.getSelectedItem())
						.getUnit_id();
				LogUtils.i("二级部门:" + unit2_id);
				// List<Unit> unit3_list=map.get(unit2_id);
				// if(unit3_list==null){
				// unit3_list=new ArrayList<Unit>();
				// unit3_list.add(new Unit("0","请选择"));
				// }
				List<Unit> unit3_list = getListUnitByParentUnitId(unit1_id,
						unit2_id);
				adapter03 = new ArrayAdapter<Unit>(RegisterActivity.this,
						android.R.layout.simple_spinner_item, unit3_list);
				adapter03
						.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				unit3_spinner.setAdapter(adapter03);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		// 三级部门spinner监听事件
		unit3_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,
					int i, long l) {
				unit3_id = ((Unit) unit3_spinner.getSelectedItem())
						.getUnit_id();
				LogUtils.i("三级部门:" + unit3_id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		role_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				role_id=((Role) role_spinner.getSelectedItem()).getRole_id();
				LogUtils.i("三级部门:" + role_id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		//邮箱输入框的监听事件
		edit_email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				isEmail = RegexUtils.checkEmail(s.toString());
//				if (!isEmail) {
//					ToastUtils.showToast(RegisterActivity.this, "邮箱格式不正确");
//				}
			}
		});
		// 手机输入框的监听事件
		edit_phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				isMobilePhone = RegexUtils.checkMobile(s.toString());
//				if (!isMobilePhone) {
//					ToastUtils.showToast(RegisterActivity.this, "手机格式不正确");
//				}
			}
		});
		//办公电话输入框的校验
		edit_office_phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				isTel=RegexUtils.checkPhone(s.toString());
//				if(!isTel){
//					ToastUtils.showToast(RegisterActivity.this, "办公电话格式不正确");
//				}
			}
		});

	}

	// 解析plist数据
	/*
	 * protected void loadUnitSpinner() { new Thread(new Runnable() {
	 * 
	 * @Override public void run() { PListXMLParser parser = new
	 * PListXMLParser();// 基于SAX的实现 PListXMLHandler handler = new
	 * PListXMLHandler(); parser.setHandler(handler); String role_json=""; try {
	 * parser.parse(getAssets().open("unit.plist"));//
	 * unit.plist是你要解析的文件，该文件需放在assets文件夹下
	 * role_json=Stringer.convert(getAssets()
	 * .open("role.json")).getBuilder().toString(); } catch
	 * (IllegalStateException e) { e.printStackTrace(); } catch (IOException e)
	 * { e.printStackTrace(); }
	 * 
	 * PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
	 * Array root_array1 = (Array) actualPList.getRootElement();
	 * unit1_list.add(new Unit("0","请选择")); for (int i = 0; i <
	 * root_array1.size(); i++) { Dict dict = (Dict) root_array1.get(i);
	 * unit1_list.add(new Unit(dict.getConfiguration("unitId") .getValue(),
	 * dict.getConfiguration("unitName") .getValue())); Array root_array2 =
	 * dict.getConfigurationArray("Units"); List<Unit> unit2_list = new
	 * ArrayList<Unit>(); unit2_list.add(new Unit("0","请选择")); for (int j = 0; j
	 * < root_array2.size(); j++) { Dict dict1 = (Dict) root_array2.get(j);
	 * unit2_list.add(new Unit(dict1 .getConfiguration("unitId").getValue(),
	 * dict1 .getConfiguration("unitName").getValue())); Array root_array3 =
	 * dict1.getConfigurationArray("Units"); List<Unit> unit3_list = new
	 * ArrayList<Unit>(); unit3_list.add(new Unit("0","请选择"));
	 * if(root_array3!=null){ for (int k = 0; k < root_array3.size(); k++) {
	 * Dict dict2 = (Dict) root_array3.get(k); unit3_list.add(new
	 * Unit(dict2.getConfiguration( "unitId").getValue(), dict2
	 * .getConfiguration("unitName").getValue())); } }
	 * map.put(dict1.getConfiguration("unitId").getValue(), unit3_list); }
	 * map.put(dict.getConfiguration("unitId").getValue(), unit2_list); }
	 * role_list.add(new Role("0","请选择")); LogUtils.i(role_json);
	 * if(!TextUtils.isEmpty(role_json)){ JSONObject jsonObject; try {
	 * jsonObject = new JSONObject(role_json); JSONArray jsonArray
	 * =jsonObject.getJSONArray("role"); for(int i=0;i<jsonArray.length();i++){
	 * JSONObject object=jsonArray.getJSONObject(i); role_list.add(new
	 * Role(object.getString("role_id"), object.getString("role_name"))); } }
	 * catch (JSONException e) { e.printStackTrace(); }
	 * 
	 * } myhandler.sendEmptyMessage(ConstantUtils.LADING_DATA); } }).start(); }
	 */

	/**
	 * @Title: 获取数据
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void loadUnitData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				unit1_list.add(new Unit("0", "请选择"));
				String role_json = "";
				try {
					json_str = Stringer.convert(getAssets().open("unit.json"))
							.getBuilder().toString();
					role_json = Stringer.convert(getAssets().open("role.json"))
							.getBuilder().toString();
					JSONObject object = new JSONObject(json_str);
					JSONArray array = object.getJSONArray("response");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						unit1_list.add(new Unit(obj.getString("unit_id"), obj
								.getString("unit_name")));
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				role_list.add(new Role("0", "请选择"));
				LogUtils.i(role_json);
				if (!TextUtils.isEmpty(role_json)) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(role_json);
						JSONArray jsonArray = jsonObject.getJSONArray("role");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							role_list.add(new Role(object.getString("role_id"),
									object.getString("role_name")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				myhandler.sendEmptyMessage(ConstantUtils.LADING_DATA);
			}
		}).start();
	}

	/**
	 * @Title: 用户注册
	 * @Description: TODO
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void sendRegisterRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
			}
		}).start();
	}

	
	class RegisterThread extends Thread{
		 public void run() { 
			 LogUtils.i("注册线程开始,.......");
			 Message msg=new Message();
				Bundle bundle=new Bundle();
				msg.what=ConstantUtils.CHECK_DATA_ERROR1;
				userName=edit_name.getText().toString();
				userEmail=edit_email.getText().toString();
				userPhone=edit_phone.getText().toString();
				userTel=edit_office_phone.getText().toString();
				userQq=edit_qq.getText().toString();
				if(TextUtils.isEmpty(userName)){//用户名不能为空
					bundle.putString("fail_info", getResources().getString(R.string.lab_register_error1));
					msg.setData(bundle);
					myhandler.sendMessage(msg);
				}else if(!isEmail){//邮箱格式不能正确
					bundle.putString("fail_info", getResources().getString(R.string.lab_register_error2));
					msg.setData(bundle);
					myhandler.sendMessage(msg);
				}else if(!TextUtils.isEmpty(userPhone)&&!isMobilePhone){//办公电话为空
						bundle.putString("fail_info", getResources().getString(R.string.lab_register_error3));
						msg.setData(bundle);
						myhandler.sendMessage(msg);
				}else if("0".equals(unit1_id)||"0".equals(unit2_id)){
					bundle.putString("fail_info", getResources().getString(R.string.lab_register_error4));
					msg.setData(bundle);
					myhandler.sendMessage(msg);
				}else if("0".equals(role_id)){
					bundle.putString("fail_info", getResources().getString(R.string.lab_register_error5));
					msg.setData(bundle);
					myhandler.sendMessage(msg);
				}else{
					Map<String, String> map=new HashMap<String, String>();
					map.put("email", userEmail);
					map.put("uname", userName);
					map.put("mphone", userPhone);
					map.put("tel_phone", userTel);
					map.put("qq", userQq);
					map.put("unit1", unit1_id);
					map.put("unit2", unit2_id);
					if("0".equals(unit3_id)){
						map.put("unit3", "");
					}else{
						map.put("unit3", unit3_id);
					}
					map.put("role_type", role_id);
					String result=HttpUtils.sendPostRequest(ConstantUtils.STR_BASE_URL+"register", map);
					LogUtils.i(result);
					myhandler.sendEmptyMessage(ConstantUtils.REGISTER_SUCCESS);
				}
		 }
	}
	/**
	 * @Title: 根据第二级查询
	 * @Description: TODO
	 * @param @param unitId
	 * @param @return 设定文件
	 * @return List<Unit> 返回类型
	 * @throws
	 */
	public List<Unit> getListUnitByParentUnitId(String unitId) {
		List<Unit> list = new ArrayList<Unit>();
		list.add(new Unit("0", "请选择"));
		JSONObject obj;
		JSONArray arr;
		if (!TextUtils.isEmpty(json_str)) {
			JSONArray array = null;
			try {
				obj = new JSONObject(json_str);
				arr = obj.getJSONArray("response");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject object = arr.getJSONObject(i);
					if (object.getString("unit_id").equals(unitId)) {
						array = object.getJSONArray("units");
						break;
					}
				}
				if (array != null) {
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						list.add(new Unit(object.getString("unit_id"), object
								.getString("unit_name")));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * @Title: 根据第一级和第二级查询
	 * @Description: TODO
	 * @param @param unitId1
	 * @param @param unitId2
	 * @param @return 设定文件
	 * @return List<Unit> 返回类型
	 * @throws
	 */
	public List<Unit> getListUnitByParentUnitId(String unitId1, String unitId2) {
		List<Unit> list = new ArrayList<Unit>();
		list.add(new Unit("0", "请选择"));
		JSONObject obj;
		JSONArray arr;
		if (!TextUtils.isEmpty(json_str)) {
			JSONArray array1 = null;
			JSONArray array2 = null;
			try {
				obj = new JSONObject(json_str);
				arr = obj.getJSONArray("response");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject object = arr.getJSONObject(i);
					if (object.getString("unit_id").equals(unitId1)) {
						array1 = object.getJSONArray("units");
						break;
					}
				}
				if (array1 != null) {
					for (int j = 0; j < array1.length(); j++) {
						JSONObject object = array1.getJSONObject(j);
						if (object.getString("unit_id").equals(unitId2)) {
							array2 = object.getJSONArray("units");
							break;
						}
					}
				}
				if (array2 != null) {
					for (int k = 0; k < array2.length(); k++) {
						JSONObject object = array2.getJSONObject(k);
						list.add(new Unit(object.getString("unit_id"), object
								.getString("unit_name")));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ConstantUtils.LADING_DATA:
				adapter01 = new ArrayAdapter<Unit>(RegisterActivity.this,
						android.R.layout.simple_spinner_item, unit1_list);
				adapter01
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				unit1_spinner.setAdapter(adapter01);
				adapter04 = new ArrayAdapter<Role>(RegisterActivity.this,
						android.R.layout.simple_spinner_item, role_list);
				adapter04
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				role_spinner.setAdapter(adapter04);
				if (dialog != null & dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			case ConstantUtils.CHECK_DATA_ERROR1:
				if(rDialog!=null&rDialog.isShowing()){
					rDialog.dismiss();
				}
				Bundle bundle=new Bundle();
				bundle=msg.getData();
				AlertDialog.Builder builder = new Builder(RegisterActivity.this); 
				builder.setTitle("提示");
				builder.setPositiveButton("确定",null); 
				builder.setIcon(android.R.drawable.ic_dialog_info); 
				builder.setMessage(bundle.getString("fail_info")); 
				builder.show();
				break;
			case ConstantUtils.REGISTER_SUCCESS:
				if(rDialog!=null&rDialog.isShowing()){
					rDialog.dismiss();
				}
				ToastUtils.showToast(RegisterActivity.this, "注册成功", Gravity.BOTTOM, 0, 40);//注册成功
				finish();
				break;
			}
		};
	};
}
