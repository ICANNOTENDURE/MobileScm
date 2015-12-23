package com.dhcc.scm.entity;

/**
 * auther hxy date 2015/12/22
 */
public class TransferInItem {
	private String init;
	// 单号
	private String trno;
	// 科室
	private String locdesc;
	// user
	private String user;

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getTrno() {
		return trno;
	}

	public void setTrno(String trno) {
		this.trno = trno;
	}

	public String getLocdesc() {
		return locdesc;
	}

	public void setLocdesc(String locdesc) {
		this.locdesc = locdesc;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
