package com.xw.test.expense;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlAuthDao;
import com.xw.restful.domain.FmlAuth;
import com.xw.restful.utils.rsa.MD5;

public class AuthTest extends ExpenseApplicationTests{
	
	@Autowired
	FmlAuthDao fmlAuthDao;

	@Test
	public void getUserByNameAndPwdTest() {
		FmlAuth userInfo = fmlAuthDao.getUserByNameAndPwd("gg", MD5.getMD5String("1234"));
		System.out.println(userInfo);
	}
}
