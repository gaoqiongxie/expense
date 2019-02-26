package com.xw.restful.domain.auth;

import java.io.Serializable;
import java.util.List;

import com.xw.restful.domain.FmlAuth;

public class UserCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userCacheKey;
	
	private FmlAuth userInfo;
	
	private List<RefreshToken> refreshTokens;
	
	private List<AccessToken> accessTokens;


	public UserCache(String userCacheKey) {
		super();
		this.userCacheKey = userCacheKey;
	}

	public UserCache() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserCacheKey() {
		return userCacheKey;
	}

	public void setUserCacheKey(String userCacheKey) {
		this.userCacheKey = userCacheKey;
	}

	public FmlAuth getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(FmlAuth userInfo) {
		this.userInfo = userInfo;
	}

	public List<RefreshToken> getRefreshTokens() {
		return refreshTokens;
	}

	public void setRefreshTokens(List<RefreshToken> refreshTokens) {
		this.refreshTokens = refreshTokens;
	}

	public List<AccessToken> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(List<AccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}
	
	

}
