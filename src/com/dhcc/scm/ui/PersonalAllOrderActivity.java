package com.dhcc.scm.ui;

import android.os.Bundle;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;

public class PersonalAllOrderActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_personal_all_order);
		super.onCreate(savedInstanceState);
		initView();
	}
}
