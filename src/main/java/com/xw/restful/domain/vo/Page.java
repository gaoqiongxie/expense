package com.xw.restful.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable{
	
	private static final long serialVersionUID = 5822688045528359215L;

	private long total; // 总记录数

	private Object rows; // 当前页中存放的记录,类型一般为List
	
	@SuppressWarnings("rawtypes")
	public Page(){
		super();
		this.total=0;
		this.rows=new ArrayList();
	}

	public Page(long total, Object rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

}
