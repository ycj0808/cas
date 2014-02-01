package com.neusoft.cas.adapter;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import com.neusoft.cas.domain.CasData;
import com.neusoft.cas.widget.R;
import com.ycj.android.common.utils.DateUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoDownloadAdapter extends BaseAdapter {

	private Context mContext;
	
	public InfoDownloadAdapter(Context context){
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		if(CasData.list_file_download!=null&&CasData.list_file_download.size()>0){
			return CasData.list_file_download.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if(CasData.list_file_download!=null){
			return CasData.list_file_download.get(position);
		}else{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		if(CasData.list_file_download!=null){
			return position;
		}else{
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item_info_download, null);
			viewHolder=new ViewHolder();
			viewHolder.Item_info_title=(TextView) convertView.findViewById(R.id.info_first_download_item_textview1);
			viewHolder.Item_info_time=(TextView) convertView.findViewById(R.id.info_download_list_item_textview2);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(CasData.list_file_download!=null&CasData.list_file_download.size()>0){
			Map<String,Object> map=CasData.list_file_download.get(position);
			String jsonStr=map.get("csfilet").toString();
			try {
				JSONObject obj=new JSONObject(jsonStr);
				viewHolder.Item_info_title.setText(obj.getString("fileName"));
				viewHolder.Item_info_time.setText(DateUtils.getDateFromLongTime(Long.valueOf(obj.getLong("uploadTime"))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return convertView;
	}

	class ViewHolder{
		TextView Item_info_title;
		TextView Item_info_time;
	}
}
