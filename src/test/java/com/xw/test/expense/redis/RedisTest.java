package com.xw.test.expense.redis;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.domain.auth.AccessToken;
import com.xw.restful.domain.auth.UserCache;
import com.xw.restful.utils.BaseUtils;
import com.xw.restful.utils.DateUtils;
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
		
//		System.out.println(MD5.getMD5String("3030"));
		
		UserCache u = (UserCache) redisUtils.get("a259bd97398a11e98919fcaa14d1bc00");
		System.out.println(BaseUtils.toJSONString(u));
		
		List<AccessToken> accessTokenList = u.getAccessTokens();
		System.out.println(BaseUtils.toJSONString(accessTokenList));
//		// 根据时间倒叙 判断是否过期 
//		
//		accessTokenList.sort(Comparator.comparing(AccessToken::getCreateTime));//升序
//		System.out.println(BaseUtils.toJSONString(accessTokenList));
//		accessTokenList.sort(Comparator.comparing(AccessToken::getCreateTime).reversed());//降序
//		System.out.println(BaseUtils.toJSONString(accessTokenList));
//		Predicate<AccessToken> expiredFilter = (at) -> ((at.getCreateTime() + (30 * 60 * 1000l)) > DateUtils.getCurrentDateMilliSecond());
//		List<AccessToken> accessTokenListFilter = accessTokenList.stream().filter(expiredFilter).collect(Collectors.toList());
//		System.out.println(BaseUtils.toJSONString(accessTokenListFilter));
//		System.out.println(BaseUtils.toJSONString(accessTokenList.stream().filter(expiredFilter).collect(Collectors.toList()).size()>0));
		
		Predicate<AccessToken> expiredFilter = (
				at) -> (/*
						 * "YTI1OWJkOTczOThhMTFlOTg5MTlmY2FhMTRkMWJjMDAjQVQjSCMzaFNPUGw_".equals(at.
						 * getAccessToken()) &&
						 */(at.getCreateTime() + (1800 * 1000l)) > DateUtils.getCurrentDateMilliSecond());
		accessTokenList.forEach((at)->{
			System.out.println("YTI1OWJkOTczOThhMTFlOTg5MTlmY2FhMTRkMWJjMDAjQVQjWEBrME4pa2c_");
			System.out.println(at.getAccessToken());
			System.out.println(at.getCreateTime() + (1800 * 1000l));
			System.out.println(DateUtils.getCurrentDateMilliSecond());
			System.out.println(at.getCreateTime() + (1800 * 1000l) > DateUtils.getCurrentDateMilliSecond());
		});
		List<AccessToken> accessTokenListFilter = accessTokenList.stream().filter(expiredFilter).collect(Collectors.toList());
		
		System.out.println(BaseUtils.toJSONString(accessTokenListFilter));
	}
	
	
	
}

