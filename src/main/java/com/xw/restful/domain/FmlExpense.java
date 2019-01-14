package com.xw.restful.domain;

import java.io.Serializable;
import java.util.Date;

public class FmlExpense implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int expenseId;

	private int memberId;
	
	private int payerId;

	private float expense;
	
	private String expenseType;

	private String expenseDesc;

	private Date expenseTime;

	private String dataState;

	private Date updateTime;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public float getExpense() {
		return expense;
	}

	public void setExpense(float expense) {
		this.expense = expense;
	}

	public String getExpenseDesc() {
		return expenseDesc;
	}

	public void setExpenseDesc(String expenseDesc) {
		this.expenseDesc = expenseDesc;
	}

	public Date getExpenseTime() {
		return expenseTime;
	}

	public void setExpenseTime(Date expenseTime) {
		this.expenseTime = expenseTime;
	}

	public String getDataState() {
		return dataState;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public int getPayerId() {
		return payerId;
	}

	public void setPayerId(int payerId) {
		this.payerId = payerId;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

}
