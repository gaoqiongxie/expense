package com.xw.restful.service;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.domain.vo.Page;
import com.xw.restful.stdo.APIRequest;

@Controller
public interface ExpenseService {

	List<FmlMember> getFmlMembers();

	Page getFmlExpenses(APIRequest apiRequest);

	List<ExpenseType> getFmlExpenseTypes();

	ExpenseVO getDtoRecordById(String id);

	int addOrUpdateExpense(APIRequest apiRequest);

}
