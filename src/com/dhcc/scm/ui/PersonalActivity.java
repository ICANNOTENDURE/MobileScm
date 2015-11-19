package com.dhcc.scm.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;

public class PersonalActivity extends BaseActivity implements OnClickListener {

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
		// TODO Auto-generated method stub

	}

}
