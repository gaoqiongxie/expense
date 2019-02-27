package com.xw.restful.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xw.restful.constant.ConstClass;
import com.xw.restful.constant.ErrorCodeEnum;
import com.xw.restful.dao.FmlAuthDao;
import com.xw.restful.domain.FmlAuth;
import com.xw.restful.domain.auth.AccessToken;
import com.xw.restful.domain.auth.RefreshToken;
import com.xw.restful.domain.auth.TokenModel;
import com.xw.restful.domain.auth.UserAuth;
import com.xw.restful.domain.auth.UserCache;
import com.xw.restful.exceptions.BizException;
import com.xw.restful.service.AuthService;
import com.xw.restful.stdo.APIRequest;
import com.xw.restful.utils.DateUtils;
import com.xw.restful.utils.ParamDataEntity;
import com.xw.restful.utils.auth.AuthUtil;
import com.xw.restful.utils.cache.CacheUtils;
import com.xw.restful.utils.redis.RedisUtils;
import com.xw.restful.utils.rsa.MD5;

@Component("authService")
@Service
public class AuthServiceImpl implements AuthService {

	private static Logger logger = Logger.getLogger(AuthServiceImpl.class);

	// private static final String uname = "gg";
	// private static final String upwd = "3030";
	// private static final int timeout = 30*60;//失效时间30 min
	private static final int TOKEN_LOW = 30 * 24 * 3600; // token 失效级别 低 30天
	// private static final int TOKEN_MAX_LOW = 10*365*24*3600;
	private static final int TOKEN_HIGH = 30 * 60;// token 失效级别 高 30分钟

	@Autowired
	FmlAuthDao fmlAuthDao;
	@Autowired
	RedisUtils redisUtils;

	@Override
	public Object login(APIRequest apiRequest) {
		logger.info("登录验证");
		ParamDataEntity paramDataEntity = new ParamDataEntity(apiRequest);
		String userCode = paramDataEntity.GetParamStringValue("userCode");
		String userPassword = paramDataEntity.GetParamStringValue("userPassword");

		// 验证用户
		FmlAuth userInfo = fmlAuthDao.getUserByNameAndPwd(userCode, MD5.getMD5String(userPassword));
		if (null == userInfo) {
			throw new BizException(ErrorCodeEnum.ERROR_LOGIN.getCode(), ErrorCodeEnum.ERROR_LOGIN.getMsg());
		}
		if (!ConstClass.INIT.equals(userInfo.getDataState())) {
			throw new BizException(ErrorCodeEnum.ERROR_USER_STATE.getCode(), ErrorCodeEnum.ERROR_USER_STATE.getMsg());
		}
		// 生成token
		TokenModel tokenModel = createToken(userInfo);

		// TODO 更新用户登录信息
		userInfo.setLastLoginTime(new Date());
		fmlAuthDao.updateLastLoginTime(userInfo);
		return tokenModel;
	}

	private TokenModel createToken(FmlAuth userInfo) {
		String key = userInfo.getId();
		UserCache userCache = (UserCache) redisUtils.get(key);
		if (null == userCache) {
			userCache = new UserCache(key);
		}
		userCache.setUserInfo(userInfo);

		// 设置 refreshToken
		RefreshToken refreshToken = updateRefreshToken(userCache, key);

		// 设置 accessToken
		AccessToken accessToken = updateAccessTokem(userCache, key);

		// 保存和更新 userCache
		redisUtils.set(key, userCache);

		UserAuth tokenResult = new UserAuth(accessToken.getAccessToken(), refreshToken.getRefreshToken(),
				accessToken.getExpireIn(), refreshToken.getExpireIn());
		TokenModel ut = new TokenModel(userInfo, tokenResult);
		return ut;
	}

	private AccessToken updateAccessTokem(UserCache userCache, String userId) {
		String accessTokenValue = AuthUtil.getAccessToken(userId);
		long expireInSecend = TOKEN_HIGH;// 剩余秒数

		// 当前的accessToken
		AccessToken accessToken = new AccessToken(accessTokenValue);
		accessToken.setExpireIn(expireInSecend);

		List<AccessToken> accessTokenList = userCache.getAccessTokens();
		if (accessTokenList != null && !accessTokenList.isEmpty()) {

			for (AccessToken at : accessTokenList) {
				long expireTime = at.getCreateTime() + (expireInSecend * 1000l);
				long currentTime = DateUtils.getCurrentDateMilliSecond();
				if (expireTime < currentTime) {// 已失效
					accessTokenList.remove(at);
				} else {
					continue;
				}
			}
			// 新增accessToken
			accessTokenList.add(accessToken);
		} else {
			accessTokenList = new CopyOnWriteArrayList<AccessToken>();
			accessTokenList.add(accessToken);
		}
		userCache.setAccessTokens(accessTokenList);
		return accessToken;
	}

	private RefreshToken updateRefreshToken(UserCache userCache, String userId) {
		String refreshTokenValue = AuthUtil.getRefreshToken(userId);
		long expireInSecend = TOKEN_LOW;// 剩余秒数
		// 当前的refreshToken
		RefreshToken refreshToken = new RefreshToken(refreshTokenValue);
		refreshToken.setExpireIn(expireInSecend);

		List<RefreshToken> refreshTokenList = userCache.getRefreshTokens();
		if (refreshTokenList != null && !refreshTokenList.isEmpty()) {

			for (RefreshToken rf : refreshTokenList) {
				long expireTime = rf.getCreateTime() + (expireInSecend * 1000l);
				long currentTime = DateUtils.getCurrentDateMilliSecond();
				if (expireTime < currentTime) {// 已失效
					refreshTokenList.remove(rf);
				} else {
					continue;
				}
			}
			// 新增refreshToken
			refreshTokenList.add(refreshToken);
		} else {
			refreshTokenList = new CopyOnWriteArrayList<RefreshToken>();
			refreshTokenList.add(refreshToken);
		}
		userCache.setRefreshTokens(refreshTokenList);
		return refreshToken;
	}

	@Override
	public Object logout(APIRequest apiRequest) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("accessToken");
		// 验证 token 是否合法
		if (!StringUtils.isEmpty(token) && CacheUtils.containsValue(token)) {
			//
			CacheUtils.clearByToken(token);
		}
		CacheUtils.printf();
		return null;
	}

	@Override
	public boolean validateAccessToken(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			throw new BizException(ErrorCodeEnum.NULL_ANTHORIZATION.getCode(),
					ErrorCodeEnum.NULL_ANTHORIZATION.getMsg());
		}
		String key = AuthUtil.getUserIdByToken(accessToken);
		UserCache userCache = (UserCache) redisUtils.get(key);
		if (null == userCache) {
			return false;
		}
		List<AccessToken> accessTokenList = userCache.getAccessTokens();
		if(accessTokenList == null || accessTokenList.isEmpty()) {
			return false;
		}
		
		boolean currentTokenFlag = false;//当前 token 是否失效
		for (AccessToken at : accessTokenList) {
			long expireTime = at.getCreateTime() + (TOKEN_HIGH * 1000l);// 剩余时间
			long currentTime = DateUtils.getCurrentDateMilliSecond();
			if (accessToken.equals(at.getAccessToken())) {
				// 验证当前token是否过期
				if (expireTime < currentTime) {// 已失效
					accessTokenList.remove(at);
					currentTokenFlag = false;
				} else {
					currentTokenFlag = true;
				}
			} else { 
				// 轮询其他token中过期的清空掉
				if (expireTime < currentTime) {// 已失效
					accessTokenList.remove(at);
				} else {
					continue;
				}
			}
		}

		if (currentTokenFlag) {
			return true;
		}

		return false;
	}
}
