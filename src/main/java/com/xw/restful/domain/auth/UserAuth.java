package com.xw.restful.domain.auth;

import java.io.Serializable;


public class UserAuth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accessToken;

	private String refreshToken;

	private long accessToken_expires_in;

	private long refreshToken_expires_in;
	
	/**     
     * 根据accessToken或者refreshToken获取userId <br/> 
     * getUserId <br/> 
     * @param authentication
     * @return  String <br/>   
     */
    public  String getUserId(String authentication) {
    	
    	
    	return null;
    }

	public UserAuth() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAuth(String accessToken, String refreshToken, long accessToken_expires_in,
			long refreshToken_expires_in) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessToken_expires_in = accessToken_expires_in;
		this.refreshToken_expires_in = refreshToken_expires_in;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getAccessToken_expires_in() {
		return accessToken_expires_in;
	}

	public void setAccessToken_expires_in(long accessToken_expires_in) {
		this.accessToken_expires_in = accessToken_expires_in;
	}

	public long getRefreshToken_expires_in() {
		return refreshToken_expires_in;
	}

	public void setRefreshToken_expires_in(long refreshToken_expires_in) {
		this.refreshToken_expires_in = refreshToken_expires_in;
	}

}
