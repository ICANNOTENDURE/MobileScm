package com.dhcc.scm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;

public class IndexActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = IndexActivity.class.getSimpleName();

	private ImageButton index_ingdrec = null;
	private ImageButton index_trf = null;
	private ImageButton index_pack = null;
	private ImageButton index_query = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		index_ingdrec = (ImageButton) findViewById(R.id.index_ingdrec_btn);
		index_ingdrec.setOnClickListener(this);
		index_trf = (ImageButton) findViewById(R.id.index_trf_btn);
		index_trf.setOnClickListener(this);
		index_pack = (ImageButton) findViewById(R.id.index_pack_btn);
		index_pack.setOnClickListener(this);
		index_query = (ImageButton) findViewById(R.id.index_query_btn);
		index_query.setOnClickListener(this);
	}

	@Override
	protected void initView() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_ingdrec_btn:
			Intent mIntent = new Intent(IndexActivity.this, InGdRecActivity.class);
			startActivity(mIntent);
			break;
		case R.id.index_trf_btn:
			break;
		case R.id.index_pack_btn:
			break;
		case R.id.index_query_btn:
			break;	
		default:
			break;
		}

	}

}
