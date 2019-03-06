package com.xw.restful.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
import com.xw.restful.utils.CollectionUtils;
import com.xw.restful.utils.DateUtils;
import com.xw.restful.utils.ParamDataEntity;
import com.xw.restful.utils.auth.AuthUtil;
import com.xw.restful.utils.redis.RedisUtils;
import com.xw.restful.utils.rsa.MD5;

@Component("authService")
@Service
public class AuthServiceImpl implements AuthService {

	private static Logger logger = Logger.getLogger(AuthServiceImpl.class);

	/** token 失效级别 低 30天*/
	private static final int TOKEN_LOW_SECOND = 30 * 24 * 3600; 
	/** token 失效级别 高 30分钟*/
	private static final int TOKEN_HIGH_SECOND = 30 * 60;

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

	/**
	 * 生成 TokenModel
	 * @param userInfo
	 * @return
	 */
	private TokenModel createToken(FmlAuth userInfo) {
		String key = userInfo.getId();
		UserCache userCache = (UserCache) redisUtils.get(key);
		if (null == userCache) {// 用户未登录
			userCache = new UserCache(key);
		}
		userCache.setUserInfo(userInfo);

		// 设置 token
		RefreshToken refreshToken = getLastedRefreshToken(userCache, key);
		AccessToken accessToken = getLastedAccessToken(userCache, key);

		// 保存和更新 userCache
		redisUtils.set(key, userCache);

		UserAuth tokenResult = new UserAuth(accessToken.getAccessToken(), refreshToken.getRefreshToken(),
				accessToken.getExpireIn(), refreshToken.getExpireIn());
		TokenModel ut = new TokenModel(userInfo, tokenResult);
		return ut;
	}

	/**
	 * 获取最新 token
	 * @param userCache
	 * @param userId
	 * @return
	 */
	private AccessToken getLastedAccessToken(UserCache userCache, String userId) {
		List<AccessToken> accessTokenList = userCache.getAccessTokens();
		if(CollectionUtils.isNullList(accessTokenList)) {
			AccessToken accessToken = createAccessToken(userId);
			accessTokenList = new CopyOnWriteArrayList<AccessToken>();
			accessTokenList.add(accessToken);
			userCache.setAccessTokens(accessTokenList);
			return accessToken;
		}
		// 过滤过期 token
		Predicate<AccessToken> expiredFilter = (at) -> ((at.getCreateTime() + (TOKEN_HIGH_SECOND * 1000l)) > DateUtils.getCurrentDateMilliSecond());
		List<AccessToken> accessTokenListUnExpired = accessTokenList.stream().filter(expiredFilter).collect(Collectors.toList());
		if(CollectionUtils.isNullList(accessTokenListUnExpired)) {
			AccessToken accessToken = createAccessToken(userId);
			accessTokenList = new CopyOnWriteArrayList<AccessToken>();
			accessTokenList.add(accessToken);
			userCache.setAccessTokens(accessTokenList);
			return accessToken;
		}
		
		userCache.setAccessTokens(accessTokenListUnExpired);
		return accessTokenListUnExpired.get(0);
	}

	/**
	 * 生成 accessToken
	 * @param userId
	 * @return
	 */
	private AccessToken createAccessToken(String userId) {
		String accessTokenValue = AuthUtil.getAccessToken(userId);
		long expireInSecend = TOKEN_HIGH_SECOND;// 剩余秒数
		AccessToken accessToken = new AccessToken(accessTokenValue);
		accessToken.setExpireIn(expireInSecend);
		
		return accessToken;
	}

	/**
	 * 获取最新 refreshToken
	 * @param userCache
	 * @param userId
	 * @return
	 */
	private RefreshToken getLastedRefreshToken(UserCache userCache, String userId) {
		List<RefreshToken> refreshTokenList = userCache.getRefreshTokens();
		if(CollectionUtils.isNullList(refreshTokenList)) {
			RefreshToken refreshToken = createRefreshToken(userId);
			refreshTokenList = new CopyOnWriteArrayList<RefreshToken>();
			refreshTokenList.add(refreshToken);
			userCache.setRefreshTokens(refreshTokenList);
			return refreshToken;
		}
		// 过滤过期 token
		Predicate<RefreshToken> expiredFilter = (at) -> ((at.getCreateTime() + (TOKEN_LOW_SECOND * 1000l)) > DateUtils.getCurrentDateMilliSecond());
		List<RefreshToken> refreshTokenListUnExpired = refreshTokenList.stream().filter(expiredFilter).collect(Collectors.toList());
		if(CollectionUtils.isNullList(refreshTokenListUnExpired)) {
			RefreshToken refreshToken = createRefreshToken(userId);
			refreshTokenList = new CopyOnWriteArrayList<RefreshToken>();
			refreshTokenList.add(refreshToken);
			userCache.setRefreshTokens(refreshTokenList);
			return refreshToken;
		}
		
		userCache.setRefreshTokens(refreshTokenListUnExpired);
		return refreshTokenListUnExpired.get(0);
	}

	/**
	 * 生成 refreshToken
	 * @param userId
	 * @return
	 */
	private RefreshToken createRefreshToken(String userId) {
		String refreshTokenValue = AuthUtil.getRefreshToken(userId);
		long expireInSecend = TOKEN_LOW_SECOND;// 剩余秒数
		RefreshToken refreshToken = new RefreshToken(refreshTokenValue);
		refreshToken.setExpireIn(expireInSecend);
		
		return refreshToken;
	}

	@Override
	public Object logout(APIRequest apiRequest) {
		ParamDataEntity paramDataEntity = new ParamDataEntity(apiRequest);
		String accessToken = paramDataEntity.GetParamStringValue("accessToken");
		String refreshToken = paramDataEntity.GetParamStringValue("refreshToken");
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
			throw new BizException(ErrorCodeEnum.NULL_ANTHORIZATION.getCode(),
					ErrorCodeEnum.NULL_ANTHORIZATION.getMsg());
		}
		String key = AuthUtil.getUserIdByToken(accessToken);
		UserCache userCache = (UserCache) redisUtils.get(key);
		if (null == userCache) {// 用户未登录
			return null;
		}

		//清除过期及当前 token
		cleanAccessToken(userCache.getAccessTokens(), accessToken);
		cleanRefreshToken(userCache.getRefreshTokens(), refreshToken);
		
		//TODO 更新redis中的userCache
		redisUtils.set(key, userCache);
		
		return null;
	}

	/**
	  *  清除失效 refreshToken
	 * @param refreshTokens
	 * @param refreshToken
	 */
	private void cleanRefreshToken(List<RefreshToken> refreshTokens, String refreshToken) {
		if (CollectionUtils.isNullList(refreshToken)) {
			return;
		}
		for (RefreshToken rt : refreshTokens) {
			long expireTime = rt.getCreateTime() + (TOKEN_LOW_SECOND * 1000l);
			long currentTime = DateUtils.getCurrentDateMilliSecond();
			if (!StringUtils.isEmpty(refreshToken) && refreshToken.equals(rt.getRefreshToken())) {
				refreshTokens.remove(rt);
			}
			if (expireTime < currentTime) {
				refreshTokens.remove(rt);
			} else {
				continue;
			}
		}
	}

	/**
	 *  清除失效 accessToken
	 * @param accessTokens
	 * @param accessToken
	 */
	private void cleanAccessToken(List<AccessToken> accessTokens, String accessToken) {
		if(CollectionUtils.isNullList(accessTokens)) {
			return;
		}
		for (AccessToken at : accessTokens) {
			long expireTime = at.getCreateTime() + (TOKEN_HIGH_SECOND * 1000l);
			long currentTime = DateUtils.getCurrentDateMilliSecond();
			if (!StringUtils.isEmpty(accessToken) && accessToken.equals(at.getAccessToken())) {
				accessTokens.remove(at);
			}
			if (expireTime < currentTime) {
				accessTokens.remove(at);
			} else {
				continue;
			}
		}
	}

	@Override
	public boolean validateAccessToken(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			throw new BizException(ErrorCodeEnum.NULL_ANTHORIZATION.getCode(),
					ErrorCodeEnum.NULL_ANTHORIZATION.getMsg());
		}
		
		return validateToken(accessToken, 0);
	}

	@Override
	public Object refresh(String refreshToken) {
		if (StringUtils.isEmpty(refreshToken)) {
			throw new BizException(ErrorCodeEnum.NULL_ANTHORIZATION.getCode(),
					ErrorCodeEnum.NULL_ANTHORIZATION.getMsg());
		}

		if (validateToken(refreshToken, 1)) {
			// userCache
			String key = AuthUtil.getUserIdByToken(refreshToken);
			UserCache userCache = (UserCache) redisUtils.get(key);

			// 设置 token
			RefreshToken resetRefreshToken = getLastedRefreshToken(userCache, key);
			AccessToken resetAccessToken = getLastedAccessToken(userCache, key);

			// 保存和更新 userCache
			redisUtils.set(key, userCache);
			
			// 保存和返回
			UserAuth tokenResult = new UserAuth(resetAccessToken.getAccessToken(), resetRefreshToken.getRefreshToken(),
					resetAccessToken.getExpireIn(), resetRefreshToken.getExpireIn());
			return tokenResult;
		} else {
			throw new BizException(ErrorCodeEnum.NO_ANTHORIZATION.getCode(), ErrorCodeEnum.NO_ANTHORIZATION.getMsg());
		}

	}

	/**
	 * token 验证
	 * @param token
	 * @param tokenType
	 * @return
	 */
	private boolean validateToken(String token, int tokenType) {
		String key = AuthUtil.getUserIdByToken(token);
		UserCache userCache = (UserCache) redisUtils.get(key);
		if (null == userCache) {// 用户未登录
			// return false;
			throw new BizException(ErrorCodeEnum.UNLOGIN.getCode(), ErrorCodeEnum.UNLOGIN.getMsg());
		}

		boolean flag = validateToken(userCache, token, tokenType);
		redisUtils.set(key, userCache);
		return flag;
	}

	/**
	 * token 验证
	 * @param userCache
	 * @param token
	 * @param tokenType
	 * @return
	 */
	private boolean validateToken(UserCache userCache, String token, int tokenType) {
		switch (tokenType) {
		case 0:
			return validateAccessToken(userCache, token);
		case 1:
			return validateRefreshToken(userCache, token);
		default:
			return false;
		}

	}

	/**
	 * 验证 refreshToken
	 * @param userCache
	 * @param token
	 * @return
	 */
	private boolean validateRefreshToken(UserCache userCache, String token) {
		List<RefreshToken> refreshTokenList = userCache.getRefreshTokens();
		if (CollectionUtils.isNullList(refreshTokenList)) {
			return false;
		}

		boolean currentTokenFlag = false;// 当前 token 是否失效
		// 过滤器 当前token 未过期
		Predicate<RefreshToken> expiredFilter = (rt) -> (token.equals(rt.getRefreshToken())
				&& (rt.getCreateTime() + (TOKEN_LOW_SECOND * 1000l)) > DateUtils.getCurrentDateMilliSecond());
		currentTokenFlag = refreshTokenList.stream().filter(expiredFilter).collect(Collectors.toList()).size() > 0;
		
		cleanRefreshToken(refreshTokenList, null);
		
		userCache.setRefreshTokens(refreshTokenList);
		
		if (currentTokenFlag) {
			return true;
		}

		return false;
	}

	/**
	 * 验证 accessToken
	 * @param userCache
	 * @param token
	 * @return
	 */
	private boolean validateAccessToken(UserCache userCache, String token) {
		List<AccessToken> accessTokenList = userCache.getAccessTokens();
		if (CollectionUtils.isNullList(accessTokenList)) {
			return false;
		}
		boolean currentTokenFlag = false;// 当前 token 是否失效
		Predicate<AccessToken> expiredFilter = (at) -> (token.equals(at.getAccessToken())
				&& (at.getCreateTime() + (TOKEN_HIGH_SECOND * 1000l)) > DateUtils.getCurrentDateMilliSecond());
		currentTokenFlag = accessTokenList.stream().filter(expiredFilter).collect(Collectors.toList()).size() > 0;
		
		cleanAccessToken(accessTokenList, null);
		
		userCache.setAccessTokens(accessTokenList);
		
		if (currentTokenFlag) {
			return true;
		}

		return false;
	}
}
