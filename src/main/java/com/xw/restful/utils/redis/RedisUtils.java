package com.xw.restful.utils.redis;

public interface RedisUtils {

	boolean hasKey(String key);
	
	Object get(String key);
	
	boolean set(String key, Object value);
	
	boolean set(String key, Object value, long time);
}
