package com.dhcc.scm.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.PersonalAllOrder;

public class PersonalAllOrderAdapter extends BaseAdapter{
	

		private Context ctx;
		
		private List<PersonalAllOrder> list;
		
		public PersonalAllOrderAdapter(Context ctx, List<PersonalAllOrder> list) {
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
		 * @time 2015/11/23
		 */
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Holder holder;
			if(convertView==null){
				holder=new Holder();
				convertView=View.inflate(ctx,R.layout.activity_personal_all_order_item, null);
				holder.batno = (TextView) convertView.findViewById(R.id.all_order_itm_batno);
				holder.name = (TextView) convertView.findViewById(R.id.all_order_itm_name);
				holder.rp = (TextView) convertView.findViewById(R.id.all_order_itm_rp);
				holder.qty = (TextView) convertView.findViewById(R.id.all_order_itm_qty);
				holder.manf = (TextView) convertView.findViewById(R.id.all_order_itm_manf);
				holder.expdate = (TextView) convertView.findViewById(R.id.all_order_itm_expdate);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			holder.batno.setText("批号" + list.get(position).getBatno());
			holder.name.setText("名称" + String.valueOf(list.get(position).getHopincname()));
			holder.rp.setText("金额" + String.valueOf(list.get(position).getRp()));
			holder.qty.setText("数量:" + String.valueOf(list.get(position).getHisqty()));
			holder.manf.setText("厂商:" + list.get(position).getManf());
			holder.expdate.setText("有效期:" + list.get(position).getExpdate());
			return convertView;
		}
		
		
		
		static class Holder {
			public TextView expdate;
			public TextView manf;
			public TextView qty;
			public TextView rp;
			public TextView name;
			public TextView batno;
			
			
		}

}
