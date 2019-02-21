package com.xw.restful.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xw.restful.anotation.Auth;
import com.xw.restful.base.BaseController;
import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.service.ExpenseService;
import com.xw.restful.stdo.APIResult;

@RestController
@Scope("prototype")
public class ExpenseController extends BaseController{
	@Autowired
	ExpenseService expenseService;

	/**
	 * 1. 获取角色
	 * @return
	 */
	@RequestMapping("/members")
	@Auth
	public APIResult members() {
		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		return new APIResult(fmlMemberList);
	}
	
	/**
	 * 1.1 新增角色
	 * @return
	 */
	@RequestMapping("/member")
	@Auth
	public APIResult member() {
		initParams();
		int count = expenseService.addMember(this.apiRequest);
		return new APIResult(count);
	}
	
	/**
	 * 2. 获取支出类别
	 * @return
	 */
	@RequestMapping("/types")
	@Auth
	public APIResult types() {
		List<ExpenseType> expenseTypes = expenseService.getFmlExpenseTypes();
		return new APIResult(expenseTypes);
	}
	
	/**
	 * 2.1 新增支出类别
	 * @return
	 */
	@RequestMapping("/type")
	@Auth
	public APIResult type() {
		initParams();
		int count = expenseService.addType(this.apiRequest);
		return new APIResult(count);
	}
	
	/**
	 * 3. 获取支出
	 * @return
	 */
	@RequestMapping("/expenses")
	@Auth
	public APIResult expenses() {
		initParams();
		return new APIResult(expenseService.getFmlExpenses(this.apiRequest));
	}
	
	/**
	 * 3.1 支出分类
	 * @return
	 */
	@RequestMapping(value="/expenses/group", method=RequestMethod.GET)
	@Auth
	public APIResult groupExpenses() {
		initParams();
		return new APIResult(expenseService.groupExpenses(this.apiRequest));
	}
	
	/**
	 * 3.2 新增、修改支出
	 * @return
	 */
	@RequestMapping("/expense")
	@Auth
	public APIResult expense() {
		initParams();
		return new APIResult(expenseService.addOrUpdateExpense(this.apiRequest));
	}

	/**
	 * 3.3 根据id获取支出
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/expense/{id}", method=RequestMethod.GET)
	@Auth
	public APIResult getDtoRecordById(@PathVariable("id") String id) {
		ExpenseVO expenseVO = expenseService.getDtoRecordById(id);
		return new APIResult(expenseVO);
	}
	
	/**
	 * 3.4 获取月支出总额
	 * @return
	 */
	@RequestMapping("/monthInfo")
	@Auth
	public APIResult monthInfo() {
		return new APIResult(expenseService.monthInfo());
	}
	
	/**
	 * 3.5 导出excel账单
	 * @param response
	 */
	@RequestMapping(value="/expenses/export", method=RequestMethod.POST)
	@Auth
	public void exportList(HttpServletResponse response) {
		initParams();
		expenseService.exportList(this.apiRequest, response);
	}
	
	/**
	 * 3.6 支出分类
	 * @return
	 */
	@RequestMapping(value="/expenses/tree", method=RequestMethod.GET)
	@Auth
	public APIResult treeExpenses() {
		initParams();
		return new APIResult(expenseService.treeExpenses(this.apiRequest));
	}
}
