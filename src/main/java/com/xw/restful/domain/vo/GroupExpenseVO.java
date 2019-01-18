package com.xw.restful.domain.vo;

import java.io.Serializable;
import java.util.List;

public class GroupExpenseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupKey;
	
	private String month;
	
	private List<GroupValue> groupValue;
	
	private boolean groupByMonth;

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public List<GroupValue> getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(List<GroupValue> groupValue) {
		this.groupValue = groupValue;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public boolean isGroupByMonth() {
		return groupByMonth;
	}

	public void setGroupByMonth(boolean groupByMonth) {
		this.groupByMonth = groupByMonth;
	}

}

