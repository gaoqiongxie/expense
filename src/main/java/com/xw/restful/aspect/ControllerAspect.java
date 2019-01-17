package com.xw.restful.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.xw.restful.utils.NetUtils;

@Aspect
@Component
public class ControllerAspect {
	private static Logger logger = Logger.getLogger(ControllerAspect.class);
	public long startTimeStamp;
	public long endTimeStamp;
	Map<String, Object> requestInfosMap = null;

	// 这个切点的表达式需要根据自己的项目来写
	@Pointcut("execution(public * com.xw.restful.controller..*(..))")
	public void log() {

	}
	
	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		startTimeStamp = System.currentTimeMillis();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		requestInfosMap = getRequestInfos(request);
		String classMethod = getClassMethod(joinPoint);
		requestInfosMap.put("class_method", classMethod);
		
		logger.info("begin-"+requestInfosMap);
	}
	

	@After("log()")
	public void doAfter() {
		endTimeStamp =  System.currentTimeMillis();
		requestInfosMap.put("timeTake(ms)", endTimeStamp-startTimeStamp);
		logger.info("end-"+requestInfosMap);
	}
	
	private String getClassMethod(JoinPoint joinPoint) {
		return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
	}
	
	public Map<String, Object> getRequestInfos(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("start_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		map.put("client_iP", NetUtils.getLocalAddress());
		map.put("request_method", request.getMethod());
		map.put("data", getRequestParamters(request));
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getRequestParamters(HttpServletRequest request) {
		// 获取所有的请求参数
		Map properties = new HashMap(request.getParameterMap());
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		// 读取map中的值
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = " ";
				returnMap = JSON.parseObject(name);
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (String value2 : values) {
					value = value2 + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			// 将读取到的值存入map中
			returnMap.put(name, value);
		}
		return returnMap;
	}
}
