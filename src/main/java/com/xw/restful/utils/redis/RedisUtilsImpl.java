package com.xw.restful.utils.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtilsImpl implements RedisUtils{
	private static Logger logger = Logger.getLogger(RedisUtilsImpl.class);

	@Autowired
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			logger.error("redis 查询出现错误");
			e.printStackTrace();
			return false;
		}
	}
	
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}
	
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean set(String key, Object value, long time) {
		try {
			if(time>0){
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
