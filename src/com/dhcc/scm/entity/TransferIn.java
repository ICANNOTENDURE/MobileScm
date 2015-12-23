package com.dhcc.scm.entity;

import java.util.List;

/**
 * auther hxy date 2015/12/22
 */
public class TransferIn {

	private String errcode;
	private List<TransferInItem> rows;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public List<TransferInItem> getRows() {
		return rows;
	}

	public void setRows(List<TransferInItem> rows) {
		this.rows = rows;
	}

}
