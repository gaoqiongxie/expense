package com.xw.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.service.ExpenseService;

@RestController
public class ExpenseController {
	@Autowired
	ExpenseService expenseService;

	@RequestMapping("/expense")
	public String expenses() {

		List<FmlMember> fmlMemberList = expenseService.getFmlMembers();
		fmlMemberList.forEach(System.out::println);

		return JSONObject.toJSONString(fmlMemberList);
	}
}
