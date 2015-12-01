package com.dhcc.scm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;

/**
 * 
 * @Title:
 * @Description: 个人信息
 * @param 设定文件
 * @return void 返回类型
 * @throws
 * @author huaxiaoying
 * @date 2015年11月20日 14:10
 */

public class PersonalActivity extends BaseActivity implements OnClickListener {

	@FindView(id = R.id.order_soon_search, click = true)
	private TextView loginBtnordersoonsearch;
	@FindView(id = R.id.nopay_order, click = true)
	private TextView nopayorder;
	@FindView(id = R.id.all_order, click = true)
	private TextView allorder;
	@FindView(id = R.id.current_user_name)
	private TextView currentusername;
	@FindView(id = R.id.personal_loc)
	private TextView personalloc;
	@FindView(id = R.id.security_group)
	private TextView securitygroup;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
		currentusername.setText("Hi~~"+LoginUser.UserName);
		personalloc.setText("您登陆科室为：" + LoginUser.LocDesc);
		securitygroup.setText("当前安全组为：" + LoginUser.UserTyp);
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
