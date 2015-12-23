package com.dhcc.scm.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dhcc.scm.R;
import com.dhcc.scm.entity.TransferInItem;

public class TransferInMainAdapter extends BaseAdapter {
	private Context ctx;
	private List<TransferInItem> list;

	public TransferInMainAdapter(Context ctx, List<TransferInItem> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.activity_tranferin_item, null);
			holder.trInit = (TextView) convertView.findViewById(R.id.trInit);
			holder.trInNo = (TextView) convertView.findViewById(R.id.trInNo);
			holder.trInLoc = (TextView) convertView.findViewById(R.id.fromLocDesc);
			holder.trInUser = (TextView) convertView.findViewById(R.id.trInUser);
			convertView.setTag(holder); // 保存至tag
		} else {
			holder = (Holder) convertView.getTag();
		}
		// get
		holder.trInit.setText(list.get(position).toString());
		holder.trInNo.setText(String.valueOf(list.get(position).getTrno().toString()));
		holder.trInLoc.setText(String.valueOf(list.get(position).getLocdesc().toString()));
		holder.trInUser.setText(String.valueOf(list.get(position).getUser().toString()));
		return convertView;
	}

	public class Holder {
		TextView trInit;
		TextView trInNo;
		TextView trInLoc;
		TextView trInUser;
	}
}
