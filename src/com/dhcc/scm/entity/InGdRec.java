package com.dhcc.scm.entity;

import java.io.Serializable;


public class InGdRec implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String scmId;
	
	private String labelId;
	
	//名称
	private String desc;
	
	private float rp;
	
	private float qty;
	
	private String batno;
	
	private String expDate;
	
	private String hisId;
	
	private String manf;
	
	private String uom;
	
	private String vendor;
	
	
	
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

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

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
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

	public String getScmId() {
		return scmId;
	}

	public void setScmId(String scmId) {
		this.scmId = scmId;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	
	
	
}
