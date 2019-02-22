package com.xw.restful.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xw.restful.service.AuthService;
import com.xw.restful.stdo.APIRequest;
import com.xw.restful.utils.ParamDataEntity;
import com.xw.restful.utils.cache.CacheUtils;
import com.xw.restful.utils.cache.CacheUtils.Load;
import com.xw.restful.utils.rsa.TOTP;

@Component("authService")
@Service
public class AuthServiceImpl implements AuthService {
	
	private static Logger logger = Logger.getLogger(AuthServiceImpl.class);
	
	private static final String uname = "gg";
	private static final String upwd = "3030";
	private static final int timeout = 30*60;//失效时间30 min

	@Override
	public Object login(APIRequest apiRequest) {
		logger.info("登录验证");
		ParamDataEntity paramDataEntity = new ParamDataEntity(apiRequest);
		String userCode = paramDataEntity.GetParamStringValue("userCode");
		String userPassword = paramDataEntity.GetParamStringValue("userPassword");
		
		String token = null;
		if(userCode.equals(uname) && userPassword.equals(upwd)) {
			token = CacheUtils.getData(uname, new Load<String>() {
	            public String load(){
	                return TOTP.generateMyTOTP(userCode, userPassword);
	            }
	        }, 20);
		}
		return token;
	}

	@Override
	public Object logout(APIRequest apiRequest) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("accessToken");
    	//验证 token 是否合法
    	if(!StringUtils.isEmpty(token) && CacheUtils.containsValue(token)){
    		//
    		CacheUtils.clearByToken(token);
    	}
    	CacheUtils.printf();
		return null;
	}
}
