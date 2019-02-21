package com.xw.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xw.restful.base.BaseController;
import com.xw.restful.service.AuthService;
import com.xw.restful.stdo.APIResult;

@RestController
@Scope("prototype")
public class AuthController extends BaseController{

	@Autowired
	AuthService authService;
	
	@RequestMapping("/login")
	public APIResult login() {
		initParams();
		return new APIResult(authService.login(this.apiRequest));
	}
	
	
	@RequestMapping("/logout")
	public APIResult logout() {
		initParams();
		return new APIResult(authService.logout(this.apiRequest));
	}
}
