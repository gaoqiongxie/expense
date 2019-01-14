package com.xw.restful.domain;

import java.io.Serializable;

public class ExpenseType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int typeId;

	private String typeName;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	

}
