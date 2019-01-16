package com.xw.restful.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	/**
	 * 1.获取角色
	 * @return
	 */
	@RequestMapping("/members")
	public APIResult members() {
		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		return new APIResult(fmlMemberList);
	}
	
	/**
	 * 2.获取支出类别
	 * @return
	 */
	@RequestMapping("/types")
	public APIResult types() {
		List<ExpenseType> expenseTypes = expenseService.getFmlExpenseTypes();
		return new APIResult(expenseTypes);
	}
	
	/**
	 * 3.获取支出
	 * @return
	 */
	@RequestMapping("/expenses")
	public APIResult expenses() {
		initParams();
		return new APIResult(expenseService.getFmlExpenses(this.apiRequest));
	}
	
	/**
	 * 3.1支出分类
	 * @return
	 */
	@RequestMapping(value="/expenses/group", method=RequestMethod.GET)
	public APIResult groupExpenses() {
		initParams();
		return new APIResult(expenseService.groupExpenses(this.apiRequest));
	}
	
	/**
	 * 3.2新增、修改支出
	 * @return
	 */
	@RequestMapping("/expense")
	public APIResult expense() {
		initParams();
		return new APIResult(expenseService.addOrUpdateExpense(this.apiRequest));
	}

	/**
	 * 3.3根据id获取支出
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/expense/{id}", method=RequestMethod.GET)
	public APIResult getDtoRecordById(@PathVariable("id") String id) {
		ExpenseVO expenseVO = expenseService.getDtoRecordById(id);
		return new APIResult(expenseVO);
	}
	
	/**
	 * 3.4获取月支出总额
	 * @return
	 */
	@RequestMapping("/monthInfo")
	public APIResult monthInfo() {
		return new APIResult(expenseService.monthInfo());
	}
	
	/**
	 * 导出excel账单
	 * @param response
	 */
	@RequestMapping(value="/expenses/export", method=RequestMethod.POST)
	public void exportList(HttpServletResponse response) {
		initParams();
		expenseService.exportList(this.apiRequest, response);
	}
}
