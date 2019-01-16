package com.xw.restful.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class ExpenseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int recordId;//记录
	
	private int expenseId;//花销
	
	private int payerId;//支出
	
	private String expenseName;
	
	private String payer;
	
	private float expense;
	
	private String expenseDesc;
	
	private Date expenseTime;
	
	private Date updateTime;
	
	private String dataState;
	
	private int typeId; //类型

	private String typeName; 

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
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

	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDataState() {
		return dataState;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	@Override
	public String toString() {
		return "ExpenseVO [recordId=" + recordId + ", expenseId=" + expenseId + ", payerId=" + payerId
				+ ", expenseName=" + expenseName + ", payer=" + payer + ", expense=" + expense + ", expenseDesc="
				+ expenseDesc + ", expenseTime=" + expenseTime + ", updateTime=" + updateTime + ", dataState="
				+ dataState + ", typeId=" + typeId + ", typeName=" + typeName + "]";
	}

}
