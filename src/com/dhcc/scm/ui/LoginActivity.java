package com.dhcc.scm.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.dhcc.scm.R;
import com.dhcc.scm.ui.base.BaseActivity;

/**
 * 
* @ClassName: LoginActivity 
* @Description: TODO(登录页面) 
* @author zhouxin
* @date 2015年7月27日 下午9:35:39 
*
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText loginaccount, loginpassword;
	private ToggleButton isShowPassword;
	private Button loginBtn, config;
	String username;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		loginaccount = (EditText) this.findViewById(R.id.loginaccount);
		loginpassword = (EditText) this.findViewById(R.id.loginpassword);

		isShowPassword = (ToggleButton) this.findViewById(R.id.isShowPassword);
		loginBtn = (Button) this.findViewById(R.id.login);
		config = (Button) this.findViewById(R.id.config);

	}

	@Override
	protected void initView() {
		
	
		config.setOnClickListener(this);
		//转换显示密码
		isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// 隐藏
					loginpassword.setInputType(0x90);
				} else {
					// 明文显示
					loginpassword.setInputType(0x81);
				}
			}
		});

		loginBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config:
			showConfig();
			break;
		case R.id.login:
			userlogin();
			break;
		default:
			break;
		}

	}


	private void userlogin() {
		username = loginaccount.getText().toString().trim();
		password = loginpassword.getText().toString().trim();

		if (username.equals("")) {
			DisplayToast("用户名不能为空!");
		}
		if (password.equals("")) {
			DisplayToast("密码不能为空!");
		}

		if (username.equals("test") && password.equals("123")) {
			DisplayToast("登錄成功!");
			
			openActivity(SplashActivity.class);
			LoginActivity.this.finish();
		}

	}
	private void showConfig() {
		openActivity(ConfigIpActivity.class);
	}

}
