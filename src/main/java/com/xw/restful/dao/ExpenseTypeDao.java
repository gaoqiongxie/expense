package com.xw.restful.dao;

import java.util.List;

import com.xw.restful.domain.ExpenseType;

public interface ExpenseTypeDao {

	List<ExpenseType> getFmlExpenseTypes();

	int replaceInto(String typeName);
	

}
