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

public class TransferOutByReqDetailAdapter  extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String,Object>> ListData;
	
	public TransferOutByReqDetailAdapter(Context context, ArrayList<HashMap<String, Object>> InListData) {
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
			convertView = mInflater.inflate(R.layout.transreq_detaillist, null);
			viewHolder = new ViewHolder();
			viewHolder.desc= (TextView) convertView.findViewById (R.id.desc);
			viewHolder.qty= (TextView) convertView.findViewById (R.id.qty);
			viewHolder.uomDesc = (TextView) convertView.findViewById (R.id.uomDesc);
			viewHolder.transedqty = (TextView) convertView.findViewById (R.id.transedqty);
			viewHolder.prvqty = (TextView) convertView.findViewById (R.id.prvqty);
			viewHolder.manf = (TextView) convertView.findViewById (R.id.manf);
			viewHolder.rp = (TextView) convertView.findViewById (R.id.rp);
			viewHolder.rowid = (TextView) convertView.findViewById (R.id.rowid);
			viewHolder.inci = (TextView) convertView.findViewById (R.id.inci);
			convertView.setTag(viewHolder); // ��ViewHolder�洢��View��
		}else{
			viewHolder = (ViewHolder) convertView.getTag();  //ȡ��ViewHolder����  
        }
		//Ȼ��������
		String prvqty = ListData.get(position).get("prvqty").toString();
		viewHolder.desc.setText(ListData.get(position).get("desc").toString());
		viewHolder.qty.setText(ListData.get(position).get("qty").toString());
		viewHolder.uomDesc.setText(ListData.get(position).get("uomDesc").toString());
		viewHolder.transedqty.setText(ListData.get(position).get("transedqty").toString());
		viewHolder.prvqty.setText(ListData.get(position).get("prvqty").toString());
		viewHolder.manf.setText(ListData.get(position).get("manf").toString());
		viewHolder.rp.setText(ListData.get(position).get("rp").toString());
		viewHolder.rowid.setText(ListData.get(position).get("rowid").toString());
		viewHolder.inci.setText(ListData.get(position).get("inci").toString());
		
		if(Double.parseDouble(prvqty)<=0){
			convertView.setBackgroundResource(R.drawable.btn_background_red);
		}else{
			convertView.setBackgroundResource(0);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView desc;
		TextView qty;
		TextView uomDesc;
		TextView transedqty;
		TextView prvqty;
		TextView manf;
		TextView rp;
		TextView rowid;
		TextView inci;
	}
}
