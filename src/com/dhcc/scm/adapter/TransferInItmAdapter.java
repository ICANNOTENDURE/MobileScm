package com.dhcc.scm.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;

public class TransferInItmAdapter  extends BaseAdapter {
	
	private ArrayList<HashMap<String,Object>> ListData;
	private LayoutInflater mInflater;
	
	public TransferInItmAdapter(Context context , ArrayList<HashMap<String,Object>> InListData){
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
			convertView = mInflater.inflate(R.layout.activity_tranferin_detitem, null);
			viewHolder = new ViewHolder();
			viewHolder.trIniti= (TextView) convertView.findViewById (R.id.trIniti);
			viewHolder.trInciDesc = (TextView) convertView.findViewById (R.id.trInciDesc);
			viewHolder.trQty = (TextView) convertView.findViewById (R.id.trQty);
			viewHolder.trUom = (TextView) convertView.findViewById (R.id.trUom);
			viewHolder.trReqQty = (TextView) convertView.findViewById (R.id.trReqQty);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
		}
		String trQty = ListData.get(position).get("TrQty").toString();
		String TrReqQty = ListData.get(position).get("TrReqQty").toString();
		//Ȼ��������
		viewHolder.trIniti.setText(ListData.get(position).get("Initi").toString());
		viewHolder.trInciDesc.setText(ListData.get(position).get("TrInciDesc").toString());
		viewHolder.trQty.setText(ListData.get(position).get("TrQty").toString());
		viewHolder.trUom.setText(ListData.get(position).get("TrUom").toString());
		viewHolder.trReqQty.setText(ListData.get(position).get("TrReqQty").toString());
		if(Double.parseDouble(trQty) != Double.parseDouble(TrReqQty)){
			viewHolder.trIniti.setTextColor(Color.RED);  
			viewHolder.trInciDesc.setTextColor(Color.RED);
			viewHolder.trQty.setTextColor(Color.RED);
			viewHolder.trUom.setTextColor(Color.RED);
			viewHolder.trReqQty.setTextColor(Color.RED);
		}
		return convertView;
	}
	
	public class ViewHolder {
		TextView trIniti;
		TextView trInciDesc;
		TextView trQty;
		TextView trUom;
		TextView trReqQty;
	};

}
