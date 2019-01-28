package com.xw.restful.dao;

import java.util.List;
import java.util.Map;

import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlExpense;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.domain.vo.GroupExpenseVO;
import com.xw.restful.domain.vo.MonthExpense;

public interface FmlExpenseDao {
	
	List<ExpenseVO> expenseVO(Map<String, Object> paMap);

	List<ExpenseType> ExpenseTypes();

	ExpenseVO getDtoRecordById(String id);

	int update(FmlExpense expense);

	int insert(FmlExpense expense);

	int countExpenses(Map<String, Object> paMap);

	int updateDataStateTodel(List<Integer> ids);

	float sumExpenseMonth();

	List<GroupExpenseVO> groupExpenses(GroupExpenseVO groupExpenseVO);

	List<MonthExpense> monthExpense();

	int insertBatch(List<FmlExpense> fmlEList);

}
