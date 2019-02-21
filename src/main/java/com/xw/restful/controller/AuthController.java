package com.xw.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xw.restful.anotation.Auth;
import com.xw.restful.base.BaseController;
import com.xw.restful.domain.FmlMember;
import com.xw.restful.service.AuthService;
import com.xw.restful.stdo.APIResult;

@RestController
@Scope("prototype")
public class AuthController extends BaseController{

	@Autowired
	AuthService authService;
	
	@RequestMapping("/login")
	public APIResult members() {
		initParams();
		return new APIResult(authService.login(this.apiRequest));
	}
}
