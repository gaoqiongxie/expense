package com.xw.restful.domain.vo;

import java.io.Serializable;

public class GroupValue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private float expenseSum;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getExpenseSum() {
		return expenseSum;
	}
	public void setExpenseSum(float expenseSum) {
		this.expenseSum = expenseSum;
	}
	
}
