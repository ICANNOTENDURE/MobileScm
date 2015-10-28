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

public class InStkTkBatDetAdapter extends BaseAdapter {
	private ArrayList<HashMap<String,Object>> ListData;
	private LayoutInflater mInflater;
	
	public InStkTkBatDetAdapter(Context context , ArrayList<HashMap<String,Object>> InListData){
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
			convertView = mInflater.inflate(R.layout.activity_instktkbyitem_mitem, null);
			viewHolder = new ViewHolder();
			viewHolder.Insti= (TextView) convertView.findViewById (R.id.Insti);
			viewHolder.Inclb = (TextView) convertView.findViewById (R.id.Inclb);
			viewHolder.BatNo = (TextView) convertView.findViewById (R.id.BatNo);
			viewHolder.ExpDate = (TextView) convertView.findViewById (R.id.ExpDate);
			viewHolder.FreUomDesc = (TextView) convertView.findViewById (R.id.FreUomDesc);
			viewHolder.FreQty = (TextView) convertView.findViewById (R.id.FreQty);
			viewHolder.CountQty = (TextView) convertView.findViewById (R.id.CountQty);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
		}
		//Ȼ��������
		viewHolder.Insti.setText(ListData.get(position).get("Insti").toString());
		viewHolder.Inclb.setText(ListData.get(position).get("Inclb").toString());
		viewHolder.BatNo.setText(ListData.get(position).get("BatNo").toString());
		viewHolder.ExpDate.setText(ListData.get(position).get("ExpDate").toString());
		viewHolder.FreUomDesc.setText(ListData.get(position).get("FreUomDesc").toString());
		viewHolder.FreQty.setText(ListData.get(position).get("FreQty").toString());
		viewHolder.CountQty.setText(ListData.get(position).get("CountQty").toString());
		return convertView;
	}
	
	
	public class ViewHolder {
		TextView Insti;
		TextView Inclb;
		TextView BatNo;
		TextView ExpDate;
		TextView FreUomDesc;
		TextView FreQty;
		TextView CountQty;
	};

}
