package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.LocAdApter;
import com.dhcc.scm.entity.Loc;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.net.IsNet;
import com.dhcc.scm.http.net.ThreadPoolUtils;
import com.dhcc.scm.http.thread.HttpPostThread;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.utils.CommonTools;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: TODO(登录页面)
 * @author zhouxin
 * @date 2015年7月27日 下午9:35:39
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText loginaccount, loginpassword, loginloc;
	private ToggleButton isShowPassword;
	private Button loginBtn, config;
	String username;
	String password;
	private List<Loc> locs = new ArrayList<Loc>();
	private LocAdApter locAdApter = null;

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
		loginloc = (EditText) this.findViewById(R.id.loginloc);
		isShowPassword = (ToggleButton) this.findViewById(R.id.isShowPassword);
		loginBtn = (Button) this.findViewById(R.id.login);
		config = (Button) this.findViewById(R.id.config);

	}

	@Override
	protected void initView() {

		loginloc.setOnClickListener(this);
		config.setOnClickListener(this);
		// 转换显示密码
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
		case R.id.loginloc:
			showLoc();
			break;
		default:
			break;
		}

	}

	private void userlogin() {
		username = loginaccount.getText().toString().trim();
		password = loginpassword.getText().toString().trim();

		if (username.isEmpty()) {
			DisplayToast("用户名不能为空!");
		}
		if (password.isEmpty()) {
			DisplayToast("密码不能为空!");
		}
		login();

	}

	private void showConfig() {
		openActivity(ConfigIpActivity.class);
	}

	/**
	 * 
	 * @Title: login
	 * @Description: TODO(登录)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author zhouxin
	 * @date 2015年10月27日 上午9:02:13
	 */
	private void login() {
		if(!loginloc.getText().toString().isEmpty()){
			 openActivity(SplashActivity.class);
			 LoginActivity.this.finish();
		}
		IsNet net = new IsNet(LoginActivity.this);
		if (!net.IsConnect()) {
			CommonTools.showShortToast(LoginActivity.this, "网络异常");
			return;
		}
		List<NameValuePair> paras = new ArrayList<NameValuePair>();
		paras.add(new BasicNameValuePair("userName", username));
		paras.add(new BasicNameValuePair("password", password));
		paras.add(new BasicNameValuePair("className", "web.DHCST.AndroidCommon"));
		paras.add(new BasicNameValuePair("methodName", "logon"));
		paras.add(new BasicNameValuePair("type", "Method"));
		ThreadPoolUtils.execute(new HttpPostThread(loginhandler, getIpByType(), paras));
	}

	Handler loginhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(LoginActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(LoginActivity.this, "服务器无响应");
			} else if (msg.what == 200) {
				try {
					JSONObject jsonObject = new JSONObject((String) msg.obj);
					if (jsonObject.getString("ErrorInfo").isEmpty()) {
						LoginUser.UserID=jsonObject.getString("UserID");
						LoginUser.UserName=jsonObject.getString("UserName");
						JSONArray locArrays = jsonObject.getJSONArray("Locs");
						for (int i = 0; i < locArrays.length(); i++) {
							JSONObject temp = (JSONObject) locArrays.get(i);
							Loc loc = new Loc(Long.valueOf(temp.getString("LocID")), temp.getString("LocDesc"));
							locs.add(loc);
						}
						loginloc.setText(locs.get(0).getName());
						LoginUser.UserLoc=String.valueOf(locs.get(0).getId());
						LoginUser.LocDesc=locs.get(0).getName();
						LoginUser.WebUrl=LoginActivity.this.getIpByType();
						CommonTools.showShortToast(LoginActivity.this, "登录成功!");

					} else {
						CommonTools.showShortToast(LoginActivity.this, "错误:" + jsonObject.getString("ErrorInfo"));
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	};

	// 弹出科室选择
	private void showLoc() {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(LoginActivity.this);
		locAdApter = new LocAdApter(LoginActivity.this, locs);
		localBuilder.setAdapter(locAdApter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Loc loc = (Loc) locAdApter.getItem(which);
				loginloc.setText(loc.getName());
				LoginUser.UserLoc=String.valueOf(loc.getId());
				LoginUser.LocDesc=loc.getName();
			}
		});
		AlertDialog localAlertDialog = localBuilder.create();
		localAlertDialog.getListView().setBackgroundColor(Color.WHITE);
		localAlertDialog.show();
	};
}
