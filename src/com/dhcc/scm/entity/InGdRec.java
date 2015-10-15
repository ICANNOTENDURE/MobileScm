package com.dhcc.scm.entity;

import java.util.Date;

public class InGdRec {
	
	//名称
	private String desc;
	
	private float rp;
	
	private float qty;
	
	private String batno;
	
	private Date expDate;
	
	private String hisId;
	
	private String manf;
	
	private String uom;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getRp() {
		return rp;
	}

	public void setRp(float rp) {
		this.rp = rp;
	}

	public float getQty() {
		return qty;
	}

	public void setQty(float qty) {
		this.qty = qty;
	}

	public String getBatno() {
		return batno;
	}

	public void setBatno(String batno) {
		this.batno = batno;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getHisId() {
		return hisId;
	}

	public void setHisId(String hisId) {
		this.hisId = hisId;
	}

	public String getManf() {
		return manf;
	}

	public void setManf(String manf) {
		this.manf = manf;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	
	
}
