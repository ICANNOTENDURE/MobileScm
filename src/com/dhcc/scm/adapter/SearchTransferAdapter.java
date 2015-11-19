package com.dhcc.scm.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dhcc.scm.R;
import com.dhcc.scm.entity.TransferOut;

public class SearchTransferAdapter extends BaseAdapter {
	private ArrayList<TransferOut> ListData;
	private LayoutInflater mInflater;
	
	public SearchTransferAdapter(Context context , ArrayList<TransferOut> InListData){
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

	@SuppressLint("InflateParams")
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
		viewHolder.stransferno.setText(ListData.get(position).getTrNo());
		viewHolder.screateDate.setText(ListData.get(position).getTrDate());
		viewHolder.stoloc.setText(ListData.get(position).getLocDesc());
		viewHolder.screateuser.setText(ListData.get(position).getUser());
		viewHolder.sinitID.setText(ListData.get(position).getInit());
		viewHolder.sStkGrpID.setText(ListData.get(position).getStkGrpID());
		viewHolder.sStkGrpDesc.setText(ListData.get(position).getStkGrpDesc());
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
