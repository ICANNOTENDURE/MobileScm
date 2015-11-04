package com.dhcc.scm.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhcc.scm.R;
import com.dhcc.scm.config.Constants;
import com.dhcc.scm.ui.annotation.FindView;
import com.dhcc.scm.ui.base.BaseActivity;

/**
 * 
 * @ClassName: ConfigIp
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhouxin
 * @date 2015年7月30日 上午10:35:12
 * 
 */
public class ConfigIpActivity extends BaseActivity implements View.OnClickListener {

	@FindView(id = R.id.config_hisurl)
	private EditText hisUrlConEditText;
	
	@FindView(id = R.id.config_scmurl)
	private EditText scmUrlConEditText;
	
	@FindView(id = R.id.configip_btn, click = true)
	private Button saveButton; // 保存明细按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_configip);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initView() {
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		hisUrlConEditText.setText(preferences.getString("HisUrl", "")); // //从内存加载config
		scmUrlConEditText.setText(preferences.getString("ScmUrl", ""));
		Toast.makeText(getApplicationContext(), "请输入.或者数字的IP", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		try {
			SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();// 获取编辑器
			editor.putString("HisUrl", hisUrlConEditText.getText().toString());
			editor.putString("ScmUrl", scmUrlConEditText.getText().toString());
			editor.commit();// 提交修改
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		} finally {
			Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
			// ConfigIpActivity.this.finish();
		}

	}

}
