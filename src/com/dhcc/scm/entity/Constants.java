package com.dhcc.scm.entity;




public class Constants {
	/**
	 ******************************************* 参数设置信息开始 ******************************************
	 */

	// 应用名称
	public static String APP_NAME = "";


	// 保存参数文件夹名称
	public static final String SHARED_PREFERENCE_NAME = "config_ip";


	// 屏幕高度
	public static int SCREEN_HEIGHT = 800;

	// 屏幕宽度
	public static int SCREEN_WIDTH = 480;

	// 屏幕密度
	public static float SCREEN_DENSITY = 1.5f;


	/**
	 ******************************************* 参数设置信息结束 ******************************************
	 */
	//his csp 地址
	public static final String HIS_CSP="csp/dhc.dhcst.androidpda.common.getdata.csp";

	
	//查询条码明细方法
	public static final String METHOD_GET_BARCODE_INFO="mobile/mobileScmCtrl!getBarCodeInfo.htm";
	
	public static final String METHOD_SEARCH_INGDREC_ITM="mobile/mobileScmCtrl!serachIngdrec.htm";
	
	//查询条码明细方法
	public static final String METHOD_SAVE_BARCODE="mobile/mobileScmCtrl!saveBarCode.htm";

	public static final String HTTPURL = null;
	
	public static final String PRO_WAIT_MESSAGE="请等待......";
	
	public static final int PAGE_SIZE=10;
	
}
