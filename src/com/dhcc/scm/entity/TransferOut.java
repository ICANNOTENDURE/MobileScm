package com.dhcc.scm.entity;


public class TransferOut {
	
	private String trNo;
	private String trDate;
	private String locDesc;
	private String user;
	private String init;
	private String StkGrpID;
	private String StkGrpDesc;
	public String getTrNo() {
		return trNo;
	}
	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}
	public String getTrDate() {
		return trDate;
	}
	public void setTrDate(String trDate) {
		this.trDate = trDate;
	}
	
	public String getLocDesc() {
		return locDesc;
	}
	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getStkGrpID() {
		return StkGrpID;
	}
	public void setStkGrpID(String stkGrpID) {
		StkGrpID = stkGrpID;
	}
	public String getStkGrpDesc() {
		return StkGrpDesc;
	}
	public void setStkGrpDesc(String stkGrpDesc) {
		StkGrpDesc = stkGrpDesc;
	}
	
}
