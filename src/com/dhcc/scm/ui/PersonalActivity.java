package com.dhcc.scm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;

public class PersonalActivity extends BaseActivity implements OnClickListener {

	@FindView(id = R.id.order_soon_search, click = true)
	private TextView loginBtnordersoonsearch;
	@FindView(id = R.id.nopay_order, click = true)
	private TextView nopayorder;
	@FindView(id = R.id.all_order, click = true)
	private TextView allorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal);
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.order_soon_search:
			Intent oIntent = new Intent(PersonalActivity.this, PersonalOederSoonSearchActivity.class);
			startActivity(oIntent);
			break;
		case R.id.nopay_order:
			Intent pIntent = new Intent(PersonalActivity.this, PersonalNopayOrderActivity.class);
			startActivity(pIntent);
			break;
		case R.id.all_order:
			Intent sIntent = new Intent(PersonalActivity.this, PersonalAllOrderActivity.class);
			startActivity(sIntent);
		default:
			break;

		}
	}
}
