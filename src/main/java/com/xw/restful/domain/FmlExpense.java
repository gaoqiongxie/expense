package com.xw.restful.domain;

import java.util.Date;

public class FmlExpense {

	int id;

	int memberId;

	float expense;

	String expenseDesc;

	Date expenseTime;

	String dataState;

	Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
}
