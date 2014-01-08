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
import com.ycj.android.common.utils.LogUtils;
import com.ycj.android.ui.utils.DialogUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends BaseMonitorActivity{

	private EditText edit_email;//邮箱
	private EditText edit_name;//姓名
	private EditText edit_phone;//手机
	private EditText edit_office_phone;//办公电话
	private EditText edit_qq;//QQ号
	private Button btn_register;//按钮
	private TextView navBar_title;
	private Spinner unit1_spinner;
	private Spinner unit2_spinner;
	private Spinner unit3_spinner;
	private Spinner role_spinner;
	private String unit1_id="0";//一级部门ID
	private String unit2_id="0";//二级部门ID
	private String unit3_id="0";//三级部门ID
	private String role_id="0";//角色ID
	private ArrayAdapter<Unit> adapter01;
	private ArrayAdapter<Unit> adapter02;
	private ArrayAdapter<Unit> adapter03;
	private ArrayAdapter<Role> adapter04;
	private Map<String, List<Unit>> map = new HashMap<String, List<Unit>>();
	private List<Role> role_list=new ArrayList<Role>();
	private List<Unit> unit1_list = new ArrayList<Unit>();
	private Dialog dialog;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		initView();
		setOnClickListener();
		View view=View.inflate(mContext, R.layout.layout_progress, null);
		TextView dialog_content=(TextView) view.findViewById(R.id.message);
		dialog_content.setText(R.string.lab_loading_data);
		dialog=DialogUtils.showProgressBar(mContext, view);
		loadUnitSpinner();
	}
	 /**
	  * @Title:初始化控件  
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void initView(){
		mContext=this;
		edit_email=(EditText) findViewById(R.id.edit_register_email);
		edit_name=(EditText) findViewById(R.id.edit_register_name);
		edit_phone=(EditText) findViewById(R.id.edit_register_phone);
		edit_office_phone=(EditText) findViewById(R.id.edit_register_office_phone);
		edit_qq=(EditText) findViewById(R.id.edit_register_qq);
		btn_register=(Button) findViewById(R.id.btn_submit);
		navBar_title=(TextView) findViewById(R.id.navbar_title);
		navBar_title.setText(R.string.register_title);
		unit1_spinner=(Spinner) findViewById(R.id.mySpinner01);
		unit2_spinner=(Spinner) findViewById(R.id.mySpinner02);
		unit3_spinner=(Spinner) findViewById(R.id.mySpinner03);
		role_spinner=(Spinner) findViewById(R.id.mySpinner04);
	}
	
	 /**
	  * @Title:设置监听事件 
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	protected void setOnClickListener(){
		//注册的监听事件
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		//一级部门spinner监听事件
		unit1_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,int i, long l) {
			    unit1_id=((Unit)unit1_spinner.getSelectedItem()).getUnit_id();
			    LogUtils.i("一级部门:"+unit1_id);
				List<Unit> unit2_list=map.get(unit1_id);
				if(unit2_list==null){
					unit2_list=new ArrayList<Unit>();
					unit2_list.add(new Unit("0","请选择"));
				}
				adapter02=new ArrayAdapter<Unit>(RegisterActivity.this, android.R.layout.simple_spinner_item,unit2_list);
				adapter02.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				unit2_spinner.setAdapter(adapter02);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		//二级部门spinner监听事件
		unit2_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,int i, long l) {
				unit2_id=((Unit)unit2_spinner.getSelectedItem()).getUnit_id();
				LogUtils.i("二级部门:"+unit2_id);
				List<Unit> unit3_list=map.get(unit2_id);
				if(unit3_list==null){
					unit3_list=new ArrayList<Unit>();
					unit3_list.add(new Unit("0","请选择"));
				}
				adapter03=new ArrayAdapter<Unit>(RegisterActivity.this, android.R.layout.simple_spinner_item,unit3_list);
				adapter03.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				unit3_spinner.setAdapter(adapter03);
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		//三级部门spinner监听事件
		unit3_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterview, View view,int i, long l) {
				unit3_id=((Unit)unit3_spinner.getSelectedItem()).getUnit_id();
				LogUtils.i("三级部门:"+unit3_id);
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
	}
	
	protected void loadUnitSpinner() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PListXMLParser parser = new PListXMLParser();// 基于SAX的实现
				PListXMLHandler handler = new PListXMLHandler();
				parser.setHandler(handler);
				String role_json="";
				try {
					parser.parse(getAssets().open("unit.plist"));// unit.plist是你要解析的文件，该文件需放在assets文件夹下
					role_json=Stringer.convert(getAssets().open("role.json")).getBuilder().toString();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
				Array root_array1 = (Array) actualPList.getRootElement();
				unit1_list.add(new Unit("0","请选择"));
				for (int i = 0; i < root_array1.size(); i++) {
					Dict dict = (Dict) root_array1.get(i);
					unit1_list.add(new Unit(dict.getConfiguration("unitId")
							.getValue(), dict.getConfiguration("unitName")
							.getValue()));
					Array root_array2 = dict.getConfigurationArray("Units");
					List<Unit> unit2_list = new ArrayList<Unit>();
					unit2_list.add(new Unit("0","请选择"));
					for (int j = 0; j < root_array2.size(); j++) {
						Dict dict1 = (Dict) root_array2.get(j);
						unit2_list.add(new Unit(dict1
								.getConfiguration("unitId").getValue(), dict1
								.getConfiguration("unitName").getValue()));
						Array root_array3 = dict1.getConfigurationArray("Units");
						List<Unit> unit3_list = new ArrayList<Unit>();
						unit3_list.add(new Unit("0","请选择"));
						if(root_array3!=null){
							for (int k = 0; k < root_array3.size(); k++) {
								Dict dict2 = (Dict) root_array3.get(k);
								unit3_list.add(new Unit(dict2.getConfiguration(
										"unitId").getValue(), dict2
										.getConfiguration("unitName").getValue()));
							}
						}
						map.put(dict1.getConfiguration("unitId").getValue(),
								unit3_list);
					}
					map.put(dict.getConfiguration("unitId").getValue(),
							unit2_list);
				}
				role_list.add(new Role("0","请选择"));
				LogUtils.i(role_json);
				if(!TextUtils.isEmpty(role_json)){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(role_json);
						JSONArray jsonArray =jsonObject.getJSONArray("role");
						for(int i=0;i<jsonArray.length();i++){
							JSONObject object=jsonArray.getJSONObject(i);
							role_list.add(new Role(object.getString("role_id"), object.getString("role_name")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				myhandler.sendEmptyMessage(ConstantUtils.LADING_DATA);
			}
		}).start();
	}
	
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ConstantUtils.LADING_DATA:
				adapter01=new ArrayAdapter<Unit>(RegisterActivity.this, android.R.layout.simple_spinner_item,unit1_list);
				adapter01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				unit1_spinner.setAdapter(adapter01);
				adapter04=new ArrayAdapter<Role>(RegisterActivity.this, android.R.layout.simple_spinner_item,role_list);
				adapter04.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				role_spinner.setAdapter(adapter04);
				if(dialog!=null&dialog.isShowing()){
					dialog.dismiss();
				}
				break;
			}
		};
	};
}
