package com.xw.test.expense;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.service.ExpenseService;

public class ExpenseTest extends ExpenseApplicationTests{
	
	private static Logger log = Logger.getLogger(ExpenseTest.class);

	@Autowired
	ExpenseService expenseService;

	@Test
	public void testGetMembers() {
		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		fmlMemberList.forEach(System.out::println);
	}
}
