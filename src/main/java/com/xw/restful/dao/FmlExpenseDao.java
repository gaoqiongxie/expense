package com.xw.restful.dao;

import java.util.List;
import java.util.Map;

import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.vo.ExpenseVO;

public interface FmlExpenseDao {
	
	List<ExpenseVO> expenseVO(Map<String, Object> paMap);

	List<ExpenseType> ExpenseTypes();

	ExpenseVO getDtoRecordById(String id);

}
