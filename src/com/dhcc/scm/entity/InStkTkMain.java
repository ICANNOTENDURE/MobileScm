package com.dhcc.scm.entity;

/**
 * auther huaxiaoying
 * date 2015/12/18
 */
import java.util.List;

public class InStkTkMain {
	
	private String errcode;
	private int count;
	private List<InStkTkMainItem> rows;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<InStkTkMainItem> getRows() {
		return rows;
	}

	public void setRows(List<InStkTkMainItem> rows) {
		this.rows = rows;
	}

}
