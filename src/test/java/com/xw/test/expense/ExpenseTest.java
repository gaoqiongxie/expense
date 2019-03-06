package com.xw.test.expense;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String, Object> paMap = new HashMap<String, Object>();
		paMap.put("expenseId", null);
		paMap.put("payerId", null);
		paMap.put("typeId", null);
		paMap.put("startTime", "2019-02-01");
		paMap.put("endTime", null);
		
		paMap.put("groupKey", "typeId");
		List<GroupExpenseVO> listByType = fmlExpenseDao.groupExpenses(paMap);
		paMap.put("groupKey", "expenseId");
		List<GroupExpenseVO> listByExpense = fmlExpenseDao.groupExpenses(paMap);
		paMap.put("groupKey", "payerId");
		List<GroupExpenseVO> listByPayer = fmlExpenseDao.groupExpenses(paMap);
		
		logger.info("根据支出类别分组：");
		logger.info(BaseUtils.toJSONString(listByType));
		logger.info("根据支出人分组：");
		logger.info(BaseUtils.toJSONString(listByExpense));
		logger.info("根据支付人分组：");
		logger.info(BaseUtils.toJSONString(listByPayer));
	}
}
