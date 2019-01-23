package com.xw.test.expense;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlExpenseDao;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.GroupExpenseVO;
import com.xw.restful.service.ExpenseService;
import com.xw.restful.utils.BaseUtils;


public class ExpenseTest extends ExpenseApplicationTests{
	
	private static Logger logger = Logger.getLogger(ExpenseTest.class);

	@Autowired
	ExpenseService expenseService;
	@Autowired
	FmlExpenseDao fmlExpenseDao;

	@Test
	public void testGetMembers() {
		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		fmlMemberList.forEach(System.out::println);
	}
	
	@Test
	public void testGroupExpense() {
		GroupExpenseVO groupVo = new GroupExpenseVO();
		groupVo.setGroupKey("typeId");
		List<GroupExpenseVO> listByType = fmlExpenseDao.groupExpenses(groupVo);
		groupVo.setGroupKey("expenseId");
		List<GroupExpenseVO> listByExpense = fmlExpenseDao.groupExpenses(groupVo);
		groupVo.setGroupKey("payerId");
		List<GroupExpenseVO> listByPayer = fmlExpenseDao.groupExpenses(groupVo);
		
		System.out.println("根据支出类别分组：");
		System.out.println(BaseUtils.toJSONString(listByType));
		System.out.println("根据支出人分组：");
		System.out.println(BaseUtils.toJSONString(listByExpense));
		System.out.println("根据支付人分组：");
		System.out.println(BaseUtils.toJSONString(listByPayer));
	}
}
