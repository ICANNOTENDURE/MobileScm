package com.dhcc.scm.ui.base;

import android.content.IntentFilter;

public enum ScanType {
	NOTIFY("android.intent.action.CONTENT_NOTIFY", "CONTENT"), 
	NOTIFY_EMII("com.ge.action.barscan", "value"), 
	NOTIFY_MOTO("ACTION_CONTENT_NOTIFY_MOTO", "com.motorolasolutions.emdk.datawedge.data_string"), 
	NOTIFY_SCAN("com.android.server.scannerservice.broadcast", "scannerdata"), 
	NOTIFY_HONEYWELL("com.android.server.scannerservice.broadcast", "scannerdata");
	// 成员变量
	private String key;
	private String value;

	// 构造方法
	private ScanType(String key, String value) {
		this.key = key;
		this.value = value;
	}

	
	public static String getKeyValue(String key) {
		for (ScanType c : ScanType.values()) {
			if (c.getKey() .equals(key)) {
				return c.getKey();
			}
		}
		return "";
	}
	
	public static IntentFilter initFilter(){
		IntentFilter intentFilter = new IntentFilter();
		for (ScanType c : ScanType.values()) {
			intentFilter.addAction(c.getKey());
		}
		return intentFilter;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
