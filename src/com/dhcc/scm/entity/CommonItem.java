package com.dhcc.scm.entity;

import java.io.Serializable;

public class CommonItem implements Serializable, Comparable<CommonItem>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int rowId;
	
	private String description;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(CommonItem arg0) {
		// TODO Auto-generated method stub
		return arg0.rowId - this.rowId;
	}
	
	 @Override
	    public boolean equals(Object o) {
	        if (o instanceof CommonItem) {
	            return ((CommonItem) o).rowId==this.rowId;
	        } else {
	            return false;
	        }
	    }
	
	
	
}
