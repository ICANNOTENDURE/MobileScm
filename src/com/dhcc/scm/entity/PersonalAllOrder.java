package com.dhcc.scm.entity;

import java.util.List;

public class PersonalAllOrder {
	 private String resultCode;
	 private String resultContent;
	 private int count;
	 private List<PersonalAllOrderItem> dataList;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultContent() {
		return resultContent;
	}
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<PersonalAllOrderItem> getDataList() {
		return dataList;
	}
	public void setDataList(List<PersonalAllOrderItem> dataList) {
		this.dataList = dataList;
	}

}
