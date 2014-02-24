package com.neusoft.cas.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.neusoft.cas.domain.CasData;
import com.neusoft.cas.widget.R;
import com.ycj.android.common.utils.DateUtils;
import com.ycj.android.common.utils.LogUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoFirstAdapter extends BaseAdapter {

	private Context mContext;
	public InfoFirstAdapter(Context context) {
		this.mContext=context;
	}

	/**
	  * @Title:获取数据数目 
	  * @Description: TODO
	  * @param @return    设定文件
	  * @return int    返回类型
	  * @throws
	  */
	@Override
	public int getCount() {
		if(CasData.list_info!=null&&CasData.list_info.size()>0){
			return CasData.list_info.size();
		}else{
			return 0;
		}
		
	}
	
	@Override
	public Object getItem(int position) {
		if(CasData.list_info!=null){
			return CasData.list_info.get(position);
		}else{
			return null;
		}
	}
	
	@Override
	public long getItemId(int position) {
		if(CasData.list_info!=null){
			return position;
		}else{
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item_info_first, null);
			viewHolder=new ViewHolder();
			viewHolder.Item_info_title=(TextView) convertView.findViewById(R.id.info_first_list_item_textview1);
			viewHolder.Item_info_time=(TextView) convertView.findViewById(R.id.info_first_list_item_textview2);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(CasData.list_info!=null&&CasData.list_info.size()>0){
			Map<String,Object> map=CasData.list_info.get(position);
			String jsonStr=map.get("csinfot").toString();
			try {
				JSONObject obj=new JSONObject(jsonStr);
				viewHolder.Item_info_title.setText(obj.getString("infoTitle"));
				//viewHolder.Item_info_time.setText(DateUtils.getDateFromLongTime(Long.valueOf(obj.getLong("publishTime"))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			viewHolder.Item_info_title.setText(map1.get("keywords").toString());
//			if(map1.get("publishTime")!=null){
//				viewHolder.Item_info_time.setText(DateUtils.getDateFromLongTime(Long.valueOf(map1.get("publishTime").toString())));
//			}
			
		}
		return convertView;
	}

	class ViewHolder{
		TextView Item_info_title;
		TextView Item_info_time;
	}
}
