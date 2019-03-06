package com.xw.test.expense;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlAuthDao;
import com.xw.restful.domain.FmlAuth;
import com.xw.restful.service.AuthService;
import com.xw.restful.utils.rsa.MD5;

public class AuthTest extends ExpenseApplicationTests{
	
	@Autowired
	FmlAuthDao fmlAuthDao;
	@Autowired
	AuthService authService;

	@Test
	public void getUserByNameAndPwdTest() {
		FmlAuth userInfo = fmlAuthDao.getUserByNameAndPwd("gg", MD5.getMD5String("1234"));
		System.out.println(userInfo);
	}
	
	@Test
	public void loginTest() {
		map.put("userCode", "gg");
		map.put("userPassword", "3030");
		o = authService.login(apiRequest);
	}
	
	@Test
	public void validateAccessTokenTest() {
		String accessToken = "YTI1OWJkOTczOThhMTFlOTg5MTlmY2FhMTRkMWJjMDAjQVQjWEBrME4pa2c_";
		o = authService.validateAccessToken(accessToken);
	}
	
	@Test
	public void logoutTest() {
		map.put("accessToken", "");
		map.put("refreshToken", "");
		o = authService.logout(apiRequest);
	}
	
	@Test
	public void refreshTest() {
		o = authService.refresh("");
	}
}
