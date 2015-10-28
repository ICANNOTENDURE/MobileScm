package com.dhcc.scm.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;

public class InStkTkMainAdapter extends BaseAdapter {

	private ArrayList<HashMap<String,Object>> ListData;
	private LayoutInflater mInflater;
	
	public InStkTkMainAdapter(Context context , ArrayList<HashMap<String,Object>> InListData){
		this.mInflater = LayoutInflater.from(context);
		ListData = InListData;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ListData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null){
			convertView = mInflater.inflate(R.layout.activity_instktk_mitem, null);
			viewHolder = new ViewHolder();
			viewHolder.istId= (TextView) convertView.findViewById (R.id.ist_rowid);
			viewHolder.istNo = (TextView) convertView.findViewById (R.id.ist_no);
			viewHolder.istTime = (TextView) convertView.findViewById (R.id.ist_time);
			viewHolder.istUser = (TextView) convertView.findViewById (R.id.ist_user);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
		}
		//Ȼ��������
		viewHolder.istId.setText(ListData.get(position).get("istId").toString());
		viewHolder.istNo.setText(ListData.get(position).get("istNo").toString());
		viewHolder.istTime.setText(ListData.get(position).get("istTime").toString());
		viewHolder.istUser.setText(ListData.get(position).get("istUser").toString());
		return convertView;
	}
	
	
	public class ViewHolder {
		TextView istId;
		TextView istNo;
		TextView istTime;
		TextView istUser;
	};

}
