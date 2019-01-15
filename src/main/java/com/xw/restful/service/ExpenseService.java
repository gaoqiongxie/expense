package com.xw.restful.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.domain.vo.Page;
import com.xw.restful.stdo.APIRequest;

@Controller
public interface ExpenseService {

	/**
	 * 获取角色
	 * @return
	 */
	List<FmlMember> getFmlMembers();

	/**
	 * 分页支出信息
	 * @param apiRequest
	 * @return
	 */
	Page getFmlExpenses(APIRequest apiRequest);

	/**
	 * 获取支出类别
	 * @return
	 */
	List<ExpenseType> getFmlExpenseTypes();

	/**
	 * 根据Id获取支出记录
	 * @param id
	 * @return
	 */
	ExpenseVO getDtoRecordById(String id);

	/**
	 * 新增或者修改
	 * @param apiRequest
	 * @return
	 */
	int addOrUpdateExpense(APIRequest apiRequest);

	/**
	 * 获取本月开销
	 * @return
	 */
	float monthInfo();

	void exportList(APIRequest apiRequest, HttpServletResponse response);

}
