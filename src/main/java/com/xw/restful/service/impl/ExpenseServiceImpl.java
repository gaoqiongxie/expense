package com.xw.restful.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xw.restful.dao.FmlExpenseDao;
import com.xw.restful.dao.FmlMemberDao;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.service.ExpenseService;

@Component("expenseService")
@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private static Logger log = Logger.getLogger(ExpenseServiceImpl.class);

	@Autowired
	FmlMemberDao fmlMemberDao;

	@Autowired
	FmlExpenseDao fmlExpenseDao;

	@Override
	public List<FmlMember> getFmlMembers() {

		return fmlMemberDao.fmlMembers();
	}

}
