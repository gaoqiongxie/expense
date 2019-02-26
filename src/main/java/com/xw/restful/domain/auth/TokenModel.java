package com.xw.restful.domain.auth;

import java.io.Serializable;

import com.xw.restful.domain.FmlAuth;

public class TokenModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FmlAuth fmlAuth;//用户信息
	
	private UserAuth userAuth;//用户请求认证信息

	public TokenModel(FmlAuth fmlAuth, UserAuth userAuth) {
		super();
		this.fmlAuth = fmlAuth;
		this.userAuth = userAuth;
	}

	public TokenModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FmlAuth getFmlAuth() {
		return fmlAuth;
	}

	public void setFmlAuth(FmlAuth fmlAuth) {
		this.fmlAuth = fmlAuth;
	}

	public UserAuth getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(UserAuth userAuth) {
		this.userAuth = userAuth;
	}

}
