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

public class TransferInMainAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String,Object>> ListData;
	
	public TransferInMainAdapter(Context context, ArrayList<HashMap<String, Object>> InListData) {
		this.mInflater = LayoutInflater.from(context);
		ListData = InListData;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ListData.size();//��������ĳ���
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_tranferin_item, null);
			viewHolder = new ViewHolder();
			viewHolder.trInit= (TextView) convertView.findViewById (R.id.trInit);
			viewHolder.trInNo= (TextView) convertView.findViewById (R.id.trInNo);
			viewHolder.trInLoc = (TextView) convertView.findViewById (R.id.fromLocDesc);
			viewHolder.trInUser = (TextView) convertView.findViewById (R.id.trInUser);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
        }
		//Ȼ��������
		viewHolder.trInit.setText(ListData.get(position).get("trInit").toString());
		viewHolder.trInNo.setText(ListData.get(position).get("trInNo").toString());
		viewHolder.trInLoc.setText(ListData.get(position).get("fromLocDesc").toString());
		viewHolder.trInUser.setText(ListData.get(position).get("trInUser").toString());
		return convertView;
	}

	public class ViewHolder {
		TextView trInit;
		TextView trInNo;
		TextView trInLoc;
		TextView trInUser;
	}
}
