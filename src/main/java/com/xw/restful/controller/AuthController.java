package com.xw.restful.controller;

import java.util.HashMap;
import java.util.Map;

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
		String accessToken = this.getRequest().getHeader("accessToken");
		String refreshToken = this.getRequest().getHeader("refreshToken");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accessToken", accessToken);
		map.put("refreshToken", refreshToken);
		this.apiRequest.setDataMap(map);
		return new APIResult(authService.logout(this.apiRequest));
	}
	
	//TODO refreshToken 重新认证
	@RequestMapping("/refresh")
	public APIResult refresh() {
		initParams();
		String refreshToken = this.getRequest().getHeader("refreshToken");
		return new APIResult(authService.refresh(refreshToken));
	}
}
