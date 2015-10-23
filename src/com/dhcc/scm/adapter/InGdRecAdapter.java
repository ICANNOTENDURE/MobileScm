package com.dhcc.scm.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.InGdRec;

public class InGdRecAdapter extends BaseAdapter {
	
	
	private Context ctx;
	
	private List<InGdRec> list;
	
	private  Map<String, String> map=new HashMap<String, String>();
	
	public InGdRecAdapter(Context ctx, List<InGdRec> list) {
		super();
		this.ctx = ctx;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if(convertView==null){
			holder=new Holder();
			convertView=View.inflate(ctx,R.layout.activity_ingdrec_item, null);
			holder.desc=(TextView) convertView.findViewById(R.id.ingdrec_itm_desc);
			holder.rp=(TextView) convertView.findViewById(R.id.ingdrec_itm_manf);
			holder.qty=(TextView) convertView.findViewById(R.id.ingdrec_itm_qty);
			holder.batno=(TextView) convertView.findViewById(R.id.ingdrec_itm_batno_exp);
			holder.manf=(TextView) convertView.findViewById(R.id.ingdrec_itm_manf);
			//使用tag存储数据
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.desc.setText(list.get(position).getDesc());
		holder.rp.setText(String.valueOf(list.get(position).getRp()));
		holder.qty.setText("数量:"+String.valueOf(list.get(position).getQty())+list.get(position).getUom());
		holder.manf.setText("厂商:"+list.get(position).getManf());
		holder.batno.setText("批号/效期:"+list.get(position).getBatno()+"/"+list.get(position).getExpDate());//+"/"+CommonTools.formatDate(list.get(position).getExpDate()));
		map.put(list.get(position).getScmId(), "1");
		return convertView;
	}
	
	
	static class Holder {
		TextView desc;
		TextView rp;
		TextView qty;
		TextView manf;
		TextView batno;
		TextView vendor;
	}
	
	public boolean checkExist(String barcode){
		return map.containsKey(barcode);
	}
}
