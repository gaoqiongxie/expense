package com.xw.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xw.restful.base.BaseController;
import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.service.ExpenseService;
import com.xw.restful.stdo.APIResult;

@RestController
public class ExpenseController extends BaseController{
	@Autowired
	ExpenseService expenseService;

	@RequestMapping("/members")
	public APIResult members() {
		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		return new APIResult(fmlMemberList);
	}
	
	@RequestMapping("/expenses")
	public APIResult expenses() {
		initParams();
		return new APIResult(expenseService.getFmlExpenses(this.apiRequest));
	}
	

	@RequestMapping("/types")
	public APIResult types() {
		List<ExpenseType> expenseTypes = expenseService.getFmlExpenseTypes();
		return new APIResult(expenseTypes);
	}
	
	@RequestMapping("/getDtoRecordById/{id}")
	public APIResult getDtoRecordById(@PathVariable("id") String id) {
		ExpenseVO expenseVO = expenseService.getDtoRecordById(id);
		return new APIResult(expenseVO);
	}
}
