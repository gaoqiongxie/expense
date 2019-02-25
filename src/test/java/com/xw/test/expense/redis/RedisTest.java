package com.xw.test.expense.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.utils.redis.RedisUtils;
import com.xw.test.expense.ExpenseApplicationTests;

public class RedisTest extends ExpenseApplicationTests{
	
	@Autowired
	RedisUtils redisUtils;
	
	@Test
	public void getTest() {
		redisUtils.set("uname", "asd");
		System.out.println(redisUtils.hasKey("uname"));
		System.out.println(redisUtils.get("uname"));
	}

}
