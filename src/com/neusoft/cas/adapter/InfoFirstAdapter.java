package com.neusoft.cas.adapter;

import java.util.List;
import java.util.Map;
import com.neusoft.cas.widget.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoFirstAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String,Object>> list_info;
	public InfoFirstAdapter(Context context,List<Map<String,Object>> list_info) {
		this.mContext=context;
		this.list_info=list_info;
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
		if(list_info!=null&&list_info.size()>0){
			return list_info.size();
		}else{
			return 0;
		}
		
	}
	
	@Override
	public Object getItem(int position) {
		if(list_info!=null){
			return list_info.get(position);
		}else{
			return null;
		}
	}
	
	@Override
	public long getItemId(int position) {
		if(list_info!=null){
			return position;
		}else{
			return 0;
		}
	}

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
		if(list_info!=null){
			Map<String,Object> map=list_info.get(position);
					
			viewHolder.Item_info_title.setText(map.get("authDesc").toString());
			viewHolder.Item_info_time.setText(map.get("authId").toString());
		}
		return convertView;
	}

	class ViewHolder{
		TextView Item_info_title;
		TextView Item_info_time;
	}
}
