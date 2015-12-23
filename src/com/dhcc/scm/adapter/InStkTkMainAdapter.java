package com.dhcc.scm.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dhcc.scm.R;
import com.dhcc.scm.entity.InStkTkMainItem;

public class InStkTkMainAdapter extends BaseAdapter {

	private Context contx;
	private List<InStkTkMainItem> list;

	public InStkTkMainAdapter(Context contx, List<InStkTkMainItem> list) {
		super();
		this.contx = contx;
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
			convertView = View.inflate(contx, R.layout.activity_instktk_mitem, null);
			holder = new Holder();
			holder.istId = (TextView) convertView.findViewById(R.id.ist_rowid);
			holder.istNo = (TextView) convertView.findViewById(R.id.ist_no);
			holder.istTime = (TextView) convertView.findViewById(R.id.ist_time);
			holder.istUser = (TextView) convertView.findViewById(R.id.ist_user);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// get
		holder.istId.setText(list.get(position).getInst());
		holder.istNo.setText(String.valueOf(list.get(position).getInstno()));
		holder.istTime.setText(String.valueOf(list.get(position).getInstdate()));
		holder.istUser.setText(String.valueOf(list.get(position).getUser()));
		return convertView;
	}

	public class Holder {
		TextView istId;
		TextView istNo;
		TextView istTime;
		TextView istUser;
	};
}