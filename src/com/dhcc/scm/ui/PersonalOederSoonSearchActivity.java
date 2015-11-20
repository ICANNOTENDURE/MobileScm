package com.dhcc.scm.ui;

import android.os.Bundle;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;

public class PersonalOederSoonSearchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_personal_oeder_soon_search);
		super.onCreate(savedInstanceState);
		initView();
	}
}
