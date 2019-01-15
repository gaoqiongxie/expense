package com.xw.restful.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xw.restful.constant.ConstClass;
import com.xw.restful.dao.ExpenseTypeDao;
import com.xw.restful.dao.FmlExpenseDao;
import com.xw.restful.dao.FmlMemberDao;
import com.xw.restful.domain.ExpenseType;
import com.xw.restful.domain.FmlExpense;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.domain.vo.ExpenseVO;
import com.xw.restful.domain.vo.Page;
import com.xw.restful.service.ExpenseService;
import com.xw.restful.stdo.APIRequest;
import com.xw.restful.utils.BaseUtils;
import com.xw.restful.utils.ParamDataEntity;

@Component("expenseService")
@Service
public class ExpenseServiceImpl implements ExpenseService{
	
	private static Logger log = Logger.getLogger(ExpenseServiceImpl.class);

	@Autowired
	FmlMemberDao fmlMemberDao;

	@Autowired
	FmlExpenseDao fmlExpenseDao;
	
	@Autowired
	ExpenseTypeDao expenseTypeDao;

	@Override
	public List<FmlMember> getFmlMembers() {

		return fmlMemberDao.fmlMembers();
	}

	@Override
	public Page getFmlExpenses(APIRequest apiRequest) {
		ParamDataEntity paramDataEntity = new ParamDataEntity(apiRequest);
		int pageN = paramDataEntity.GetParamIntValue("page", 1);
		int rows = paramDataEntity.GetParamIntValue("rows", 10);
		pageN = BaseUtils.getPage(rows, pageN);
		
		String expenseId = paramDataEntity.GetParamStringValue("sExpense", null);
		String payerId = paramDataEntity.GetParamStringValue("sPayer", null);
		String typeId = paramDataEntity.GetParamStringValue("sType", null);
		
		String startTime = paramDataEntity.GetParamStringValue("startTime", null);
		String endTime = paramDataEntity.GetParamStringValue("endTime", null);
		
		Map<String, Object> paMap = new HashMap<String, Object>();
		paMap.put("page", pageN);
		paMap.put("pageSize", rows);
		paMap.put("expenseId", expenseId);
		paMap.put("payerId", payerId);
		paMap.put("typeId", typeId);
		paMap.put("startTime", startTime);
		paMap.put("endTime", endTime);
		
		
		int count = fmlExpenseDao.countExpenses(paMap);
		List<ExpenseVO> expenses = fmlExpenseDao.expenseVO(paMap);
		
		Page page = new Page(count, expenses);
		return page;
	}

	@Override
	public List<ExpenseType> getFmlExpenseTypes() {
		return expenseTypeDao.getFmlExpenseTypes();
	}

	@Override
	public ExpenseVO getDtoRecordById(String id) {
		if(StringUtils.isEmpty(id)) return null;
		
		return fmlExpenseDao.getDtoRecordById(id);
	}

	@Override
	public int addOrUpdateExpense(APIRequest apiRequest) {
		ParamDataEntity paramDataEntity = new ParamDataEntity(apiRequest);
		FmlExpense expense = paramDataEntity.getObject(FmlExpense.class);
		String actionType = paramDataEntity.GetParamStringValue("actionType");
		switch (actionType) {
		case ConstClass.EDIT:
			return fmlExpenseDao.update(expense);
		case ConstClass.ADD:
			return fmlExpenseDao.insert(expense);
		case ConstClass.DEL:
			String ids =  paramDataEntity.GetParamStringValue("ids");
			return fmlExpenseDao.updateDataStateTodel(BaseUtils.stringToIntegerList(ids));
		default:
			return 0;
		}
	}

}
