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
import com.xw.restful.utils.HttpRequestUtils;

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
	
	private Map<String, Object> getRequestInfos(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("start_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		map.put("client_iP", NetUtils.getLocalAddress());
		map.put("request_method", request.getMethod());
		map.put("data", HttpRequestUtils.getRequestParamters(request));
		return map;
	}
	
}
