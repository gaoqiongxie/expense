
package com.xw.test.expense.tree;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlExpenseDao;
import com.xw.restful.domain.vo.MonthExpense;
import com.xw.restful.utils.BaseUtils;
import com.xw.restful.utils.ListUtils;
import com.xw.test.expense.ExpenseApplicationTests;

public class TreeTest extends ExpenseApplicationTests {

	private static Logger logger = Logger.getLogger(TreeTest.class);

	@Autowired
	FmlExpenseDao fmlExpenseDao;

	@Test
	public void testMonthExpense() {
		List<MonthExpense> list = fmlExpenseDao.monthExpense();

		// 多字段分组
		Map<String, Map<String, Map<String, Map<String, List<MonthExpense>>>>> groupBy = list.stream().collect(
				Collectors.groupingBy(MonthExpense::getMonth, Collectors.groupingBy(MonthExpense::getPayer, Collectors
						.groupingBy(MonthExpense::getExpenseName, Collectors.groupingBy(MonthExpense::getTypeName)))));
//		System.out.println(BaseUtils.toJSONString(groupBy));
		
//		List<String> months = list.stream().map(MonthExpense::getMonth).distinct().collect(Collectors.toList());
		
		List relist = ListUtils.mapToList(groupBy);
		System.out.println(BaseUtils.toJSONString(relist));

	}

}