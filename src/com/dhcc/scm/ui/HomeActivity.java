package com.dhcc.scm.ui;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.dhcc.scm.AppManager;
import com.dhcc.scm.R;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {

	public static final String TAG = HomeActivity.class.getSimpleName();

	private RadioGroup mTabButtonGroup;
	private TabHost mTabHost;

	public static final String TAB_MAIN = "MAIN_ACTIVITY";
	public static final String TAB_SEARCH = "SEARCH_ACTIVITY";
	public static final String TAB_CATEGORY = "CATEGORY_ACTIVITY";
	public static final String TAB_CART = "CART_ACTIVITY";
	public static final String TAB_PERSONAL = "PERSONAL_ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_home);
		findViewById();
		initView();
	}

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
	}

	private void initView() {

		mTabHost = getTabHost();

		Intent i_main = new Intent(this, IndexActivity.class);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN).setContent(i_main));
		mTabHost.setCurrentTabByTag(TAB_MAIN);
		mTabButtonGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.home_tab_main:
					mTabHost.setCurrentTabByTag(TAB_MAIN);
					break;

				case R.id.home_tab_search:
					mTabHost.setCurrentTabByTag(TAB_SEARCH);
					break;

				case R.id.home_tab_category:
					mTabHost.setCurrentTabByTag(TAB_CATEGORY);
					break;

				case R.id.home_tab_cart:
					mTabHost.setCurrentTabByTag(TAB_CART);
					break;

				case R.id.home_tab_personal:
					Intent mIntent = new Intent(HomeActivity.this, PersonalActivity.class);
					startActivity(mIntent);
					// mTabHost.setCurrentTabByTag(TAB_PERSONAL);
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_about:

			break;

		case R.id.menu_setting:

			break;

		case R.id.menu_history:

			break;

		case R.id.menu_feedback:

			break;

		case R.id.menu_exit:

			showAlertDialog("退出程序", "确定退出东华供应链平台吗？", "确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AppManager.getInstance().AppExit(getApplicationContext());
					ImageLoader.getInstance().clearMemoryCache();
				}
			}, "取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			break;

		case R.id.menu_help:

			break;

		default:
			break;
		}
		return true;
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected void showAlertDialog(String title, String message, String positiveText, DialogInterface.OnClickListener onPositiveClickListener, String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(positiveText, onPositiveClickListener).setNegativeButton(negativeText, onNegativeClickListener).show();
	}

}
