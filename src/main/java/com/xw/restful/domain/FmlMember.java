package com.xw.restful.domain;

import java.io.Serializable;

public class FmlMember implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int memberId;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}
