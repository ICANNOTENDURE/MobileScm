package com.dhcc.scm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.annotation.FindView;
import com.dhcc.scm.ui.base.BaseActivity;

public class IndexActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = IndexActivity.class.getSimpleName();
	
	@FindView(id=R.id.index_ingdrec_btn,click=true)
	private ImageButton index_ingdrec;
	
	@FindView(id=R.id.index_trf_btn,click=true)
	private ImageButton index_trf ;
	
	@FindView(id=R.id.index_pack_btn,click=true)
	private ImageButton index_pack = null;
	
	@FindView(id=R.id.index_query_btn,click=true)
	private ImageButton index_query = null;
	
	@FindView(id=R.id.index_trfin_btn,click=true)
	private ImageButton index_trfin = null;
	
	@FindView(id=R.id.index_trfByReq_btn,click=true)
	private ImageButton index_trfbyreq = null;
	
	@FindView(id=R.id.index_stk_btn,click=true)
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
