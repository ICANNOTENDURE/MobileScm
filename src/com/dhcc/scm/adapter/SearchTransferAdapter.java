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

public class SearchTransferAdapter extends BaseAdapter {
	private ArrayList<HashMap<String,Object>> ListData;
	private LayoutInflater mInflater;
	
	public SearchTransferAdapter(Context context , ArrayList<HashMap<String,Object>> InListData){
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
			convertView = mInflater.inflate(R.layout.search_transferout_item, null);
			viewHolder = new ViewHolder();
			viewHolder.stransferno= (TextView) convertView.findViewById (R.id.s_transferno);
			viewHolder.screateDate = (TextView) convertView.findViewById (R.id.s_createDate);
			viewHolder.stoloc = (TextView) convertView.findViewById (R.id.s_toloc);
			viewHolder.screateuser = (TextView) convertView.findViewById (R.id.s_createuser);
			viewHolder.sinitID = (TextView) convertView.findViewById (R.id.s_initID);
			viewHolder.sStkGrpID = (TextView) convertView.findViewById (R.id.s_catgrpid);
			viewHolder.sStkGrpDesc = (TextView) convertView.findViewById (R.id.s_catgrpdesc);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
		}
		//Ȼ��������
		viewHolder.stransferno.setText(ListData.get(position).get("trInNo").toString());
		viewHolder.screateDate.setText(ListData.get(position).get("trDate").toString());
		viewHolder.stoloc.setText(ListData.get(position).get("toLocDesc").toString());
		viewHolder.screateuser.setText(ListData.get(position).get("trUser").toString());
		viewHolder.sinitID.setText(ListData.get(position).get("trInit").toString());
		viewHolder.sStkGrpID.setText(ListData.get(position).get("StkGrpID").toString());
		viewHolder.sStkGrpDesc.setText(ListData.get(position).get("StkGrpDesc").toString());
		return convertView;
	}

	public class ViewHolder {
		TextView stransferno;
		TextView screateDate;
		TextView stoloc;
		TextView screateuser;
		TextView sinitID;
		TextView sStkGrpID;
		TextView sStkGrpDesc;
	}

}
