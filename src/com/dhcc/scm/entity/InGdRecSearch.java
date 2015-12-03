package com.dhcc.scm.entity;

import java.util.List;

public class InGdRecSearch {
	
	 private String resultCode;
	 private String resultContent;
	 private int count;
	 private List<InGdRecSearchItm> dataList;
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
	public List<InGdRecSearchItm> getDataList() {
		return dataList;
	}
	public void setDataList(List<InGdRecSearchItm> dataList) {
		this.dataList = dataList;
	}
	
	 
	 

}
