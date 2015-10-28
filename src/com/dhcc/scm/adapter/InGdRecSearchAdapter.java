package com.dhcc.scm.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.InGdRecSearch;

public class InGdRecSearchAdapter extends BaseAdapter {

	private Context ctx;
	
	private List<InGdRecSearch> list;
	
	public InGdRecSearchAdapter(Context ctx, List<InGdRecSearch> list) {
		super();
		this.ctx = ctx;
		this.list = list;

	}
	@Override
	public int getCount() {
		
		return list.size();
	}
	@Override
	public Object getItem(int arg0) {
		
		return list.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		
		return 0;
	}
	/**
	 * @author huaxiaoying
	 * 作用代码
	 */
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if(convertView==null){
			holder=new Holder();
			convertView=View.inflate(ctx,R.layout.activity_ingdrec_search_result_item, null);
			holder.num=(TextView) convertView.findViewById(R.id.ingdre_search_itm_num);
			holder.home=(TextView) convertView.findViewById(R.id.ingdrec_search_itm_home);
			//使用tag存储数据
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.num.setText(list.get(position).getNum());
		holder.home.setText("厂家"+String.valueOf(list.get(position).getHome()));
		return convertView;
	}
	
	
	
	static class Holder {
		TextView num;
		TextView home;
		
	}
}
