package com.xw.restful.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xw.restful.constant.ErrorCodeEnum;
import com.xw.restful.stdo.APIResult;
import com.xw.restful.utils.HttpRequestUtils;
import com.xw.restful.utils.cache.CacheUtils;

@Aspect
@Component
public class AuthAspect {
	private static Logger logger = Logger.getLogger(AuthAspect.class);
	
	@Pointcut("execution(public * com.xw.restful.controller..*(..)) && @annotation(com.xw.restful.anotation.Auth)" )
    public void anthorization(){} 
	
    @SuppressWarnings("unchecked")
	@Around("anthorization()")
    public Object Interceptor(ProceedingJoinPoint pjp){
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	String token = request.getHeader("accessToken");
    	
    	logger.info("验证 token 是否合法-accessToken-"+token);
    	//验证 token 是否合法
    	if(StringUtils.isEmpty(token)){
    		return new APIResult(ErrorCodeEnum.NULL_ERROR.getCode(), ErrorCodeEnum.NULL_ERROR.getMsg());
    	}else if(!CacheUtils.containsValue(token)) {
    		return new APIResult(ErrorCodeEnum.NO_ANTHORIZATION.getCode(), ErrorCodeEnum.NO_ANTHORIZATION.getMsg());
    	}else {
    		//TODO 更新过期时间
    		
    		Object result = null; 
            try {
                result = pjp.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }  
            return result;
    	}
        
    }
}
