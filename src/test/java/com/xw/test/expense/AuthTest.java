package com.xw.test.expense;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlAuthDao;
import com.xw.restful.domain.FmlAuth;
import com.xw.restful.domain.auth.TokenModel;
import com.xw.restful.domain.auth.UserAuth;
import com.xw.restful.service.AuthService;
import com.xw.restful.utils.BaseUtils;
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
	
	@Test
	public void authTest() {
		//login
		TokenModel tokenModel_login = login("gg", "3030");
		if(null == tokenModel_login) {
			printf("login fail");
			return;
		}
		printf("login success");
		printfJSON(tokenModel_login);
		
		//validate
		printf(validate(tokenModel_login));
		
		printf("睡眠 30 分钟");
		sleepMinute(30);
		
		//refresh
		String refreshToken = tokenModel_login.getUserAuth().getRefreshToken();
		UserAuth userAuth = (UserAuth) authService.refresh(refreshToken);
		if(null == userAuth) {
			printf("refreshToken fail");
			return;
		}
		TokenModel tokenModel_refresh = new TokenModel();
		try {
			BeanUtils.copyProperties(tokenModel_login, tokenModel_refresh);
			tokenModel_refresh.setUserAuth(userAuth);
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		printf("refresh success");
		printfJSON(tokenModel_refresh);
		
		
		//validate
		printf(validate(tokenModel_refresh));
		
		//logout
		
		
		
	}
	
	private TokenModel login(String userCode, String userPassword) {
		map.put("userCode", "gg");
		map.put("userPassword", "3030");
		TokenModel tokenModel = (TokenModel) authService.login(apiRequest);
		return tokenModel;
	}

	private boolean validate(TokenModel tokenModel) {
		String acceessToken = tokenModel.getUserAuth().getAccessToken();
		return authService.validateAccessToken(acceessToken);
	}

	private void printf(Object o) {
		System.out.println(o);
	}
	
	private void printfJSON(Object o) {
		System.out.println(BaseUtils.toJSONString(o));
	}
	
	private void sleepMinute(long minute) {
		try {
			new Thread();
			printf("睡眠 "+minute+" 分钟");
			Thread.sleep(minute*60*1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
