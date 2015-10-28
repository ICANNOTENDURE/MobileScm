package com.dhcc.scm.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.Loc;

public class LocAdApter extends BaseAdapter{
	
	private Context mContext;
	
	private List<Loc> locs;
	
	
	
	public LocAdApter(Context mContext, List<Loc> locs) {
		super();
		this.mContext = mContext;
		this.locs = locs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return locs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return locs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder;
		if(convertView==null){
			holder=new Holder();
			convertView=View.inflate(mContext,R.layout.loc_item, null);
			holder.id=(TextView) convertView.findViewById(R.id.LocID);
			holder.name=(TextView) convertView.findViewById(R.id.LocDesc);
			//使用tag存储数据
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.id.setText(locs.get(position).getId().toString());
		holder.name.setText(String.valueOf(locs.get(position).getName()));
		return convertView;
	}
	
	static class Holder {
		TextView id;
		TextView name;
	}

}
