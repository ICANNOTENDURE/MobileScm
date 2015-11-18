package com.dhcc.scm.ui.base;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dhcc.scm.AppManager;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.utils.AnnotateUtil;

public abstract class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getSimpleName();

	protected Handler mHandler = null;
	protected InputMethodManager imm;
	private TelephonyManager tManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
		

		tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//注解绑定属性
		AnnotateUtil.initBindView(this);
		
	}

	/**
	 * 绑定控件id
	 */
	protected  void findViewById(){};

	/**
	 * 初始化控件
	 */
	protected  void initView(){};
	
    /**
     * 初始化数据
     */
	protected void initData(){};



    /**
     * 初始化控件
     */
    void initWidget(){};

    /**
     * 点击事件回调方法
     */
    void widgetClick(View v){};

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}



	protected void hideOrShowSoftInput(boolean isShowSoft, EditText editText) {
		if (isShowSoft) {
			imm.showSoftInput(editText, 0);
		} else {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}

	// 获得当前程序版本信息
	protected String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		return packInfo.versionName;
	}

	// 獲得設備信息
	protected String getDeviceId() throws Exception {
		String deviceId = tManager.getDeviceId();

		return deviceId;

	}

	/**
	 * 获取SIM卡序列号
	 * 
	 * @return
	 */
	protected String getToken() {
		return tManager.getSimSerialNumber();
	}

	/* 獲得系統版本 */

	protected String getClientOs() {
		return android.os.Build.ID;
	}

	/* 獲得系統版本號 */
	protected String getClientOsVer() {
		return android.os.Build.VERSION.RELEASE;
	}

	// 獲得系統語言包
	protected String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	protected String getCountry() {
		return Locale.getDefault().getCountry();
	}

	protected String getIpByType(String ... type){
		
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		String hisIp= "http://"+preferences.getString("HisUrl", "")+"/dthealth/web/"+Constants.HIS_CSP; // //从内存加载config
		String scmIp= "http://"+preferences.getString("ScmUrl", "")+"/scm/"; // //从内存加载config
		return type.length==0?hisIp:scmIp;
	}


}
