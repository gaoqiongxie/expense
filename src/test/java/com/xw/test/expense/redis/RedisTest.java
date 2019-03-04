package com.xw.test.expense.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.domain.auth.UserCache;
import com.xw.restful.utils.BaseUtils;
import com.xw.restful.utils.redis.RedisUtils;
import com.xw.restful.utils.rsa.MD5;
import com.xw.test.expense.ExpenseApplicationTests;

public class RedisTest extends ExpenseApplicationTests{
	
	@Autowired
	RedisUtils redisUtils;
	
	@Test
	public void getTest() {
//		redisUtils.set("uname", "asd");
//		System.out.println(redisUtils.hasKey("uname"));
//		System.out.println(redisUtils.get("uname"));
		
		System.out.println(MD5.getMD5String("3030"));
		
		UserCache u = (UserCache) redisUtils.get("a259bd97398a11e98919fcaa14d1bc00");
		System.out.println(BaseUtils.toJSONString(u));
	}
	
	
	
}

