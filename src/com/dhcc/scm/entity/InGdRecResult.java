package com.dhcc.scm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InGdRecResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codeType;	
	
	private String resultCode="1";
	
	private String resultComtent="";
	
	private List<InGdRec> gdRecItms=new ArrayList<InGdRec>();

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultComtent() {
		return resultComtent;
	}

	public void setResultComtent(String resultComtent) {
		this.resultComtent = resultComtent;
	}

	public List<InGdRec> getGdRecItms() {
		return gdRecItms;
	}

	public void setGdRecItms(List<InGdRec> gdRecItms) {
		this.gdRecItms = gdRecItms;
	}
	
	
	
}
