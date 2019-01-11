package com.xw.restful.service;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.xw.restful.domain.FmlMember;

@Controller
public interface ExpenseService {

	List<FmlMember> getFmlMembers();

}
