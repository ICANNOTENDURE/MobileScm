package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.LocAdApter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.Loc;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.http.net.IsNet;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.CommonTools;
import com.dhcc.scm.utils.Loger;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: TODO(登录页面)
 * @author zhouxin
 * @date 2015年7月27日 下午9:35:39
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	
	@FindView(id = R.id.login, click = true)
	private Button loginBtn; // 登陆

	@FindView(id = R.id.loginloc, click = true)
	private EditText loginloc; // 登录科室

	@FindView(id = R.id.loginaccount)
	private EditText loginaccount; //

	@FindView(id = R.id.loginpassword)
	private EditText loginpassword; //

	@FindView(id = R.id.isShowPassword)
	private ToggleButton isShowPassword; // 密码显示隐藏切换

	@FindView(id = R.id.config, click = true)
	private Button config; //
	
	//进度条
	private ProgressDialog progressDialog;
	String username;
	String password;
	private List<Loc> locs = new ArrayList<Loc>();
	private LocAdApter locAdApter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected void initView() {

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
			ViewInject.toast("用户名不能为空!");
		}
		if (password.isEmpty()) {
			ViewInject.toast("密码不能为空!");
		}
		progressDialog=ViewInject.getprogress(LoginActivity.this, Constants.PRO_WAIT_MESSAGE, false);
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
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			CommonTools.showShortToast(LoginActivity.this, "网络异常");
			return;
		}
		
		
	
		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		Http http = new Http(config);
		HttpParams params = new HttpParams();
		params.put("requestType", "apk");
		params.put("userName", username);
		params.put("password", password);
		params.put("className", "web.DHCST.AndroidCommon");
		params.put("methodName", "logon");
		params.put("type", "Method");
		http.post(getIpByType(), params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				if (t != null) {
					String str = new String(t);
					Loger.debug("登陆网络请求：" + new String(t));
					try {
						Log.i("dhcc", str);
						JSONObject jsonObject = new JSONObject(str);
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
							ViewInject.toast("登录成功!");

						} else {
							ViewInject.toast("错误:" + jsonObject.getString("ErrorInfo"));
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						ViewInject.toast(e.getMessage());
					}
				}
			}

			;
		});
	}
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
