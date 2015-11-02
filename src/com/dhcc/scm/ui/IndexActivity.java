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
	private ImageButton index_trfin = null;
	private ImageButton index_trfbyreq = null;
	private ImageButton index_stk = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_index);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		index_ingdrec = (ImageButton) findViewById(R.id.index_ingdrec_btn);
		index_trf = (ImageButton) findViewById(R.id.index_trf_btn);
		index_pack = (ImageButton) findViewById(R.id.index_pack_btn);
		index_query = (ImageButton) findViewById(R.id.index_query_btn);
		index_trfin = (ImageButton) findViewById(R.id.index_trfByReq_btn);
		index_trfbyreq = (ImageButton) findViewById(R.id.index_trfin_btn);
		index_stk= (ImageButton) findViewById(R.id.index_stk_btn);
	}

	@Override
	protected void initView() {
		index_ingdrec.setOnClickListener(this);
		index_trf.setOnClickListener(this);
		index_pack.setOnClickListener(this);
		index_query.setOnClickListener(this);
		index_trfin.setOnClickListener(this);
		index_trfbyreq.setOnClickListener(this);
		index_stk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_ingdrec_btn:
			Intent mIntent = new Intent(IndexActivity.this, InGdRecActivity.class);
			startActivity(mIntent);
			break;
		case R.id.index_trf_btn:
			startActivity(new Intent(IndexActivity.this, TransferOut.class));
			break;
		case R.id.index_pack_btn:
			startActivity(new Intent(IndexActivity.this, UpItmListActivity.class));
			break;
		case R.id.index_trfByReq_btn:
			startActivity(new Intent(IndexActivity.this, TransferOutByReqActivity.class));
			break;
		case R.id.index_trfin_btn:
			startActivity(new Intent(IndexActivity.this, TransferInActivity.class));
			break;
		case R.id.index_query_btn:
			startActivity(new Intent(IndexActivity.this, InGdRecSearchActivity.class));
			break;
		case R.id.index_stk_btn:
			startActivity(new Intent(IndexActivity.this, InStkTkMainActivity.class));
			break;	
		default:
			break;
		}

	}

}
