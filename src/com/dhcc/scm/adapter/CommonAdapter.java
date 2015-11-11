package com.dhcc.scm.adapter;

import java.util.Collection;

import android.widget.AbsListView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.CommonItem;
import com.dhcc.scm.ui.base.DhcAdapter;
import com.dhcc.scm.widgets.AdapterHolder;

public class CommonAdapter extends DhcAdapter<CommonItem> {

	public CommonAdapter(AbsListView view, Collection<CommonItem> mDatas, int itemLayoutId) {
		super(view, mDatas, itemLayoutId);
	}

	@Override
	public void convert(AdapterHolder helper, CommonItem item, boolean isScrolling) {
		helper.setText(R.id.RowId, String.valueOf(item.getRowId()));
		helper.setText(R.id.Description, item.getDescription());
	}
}
