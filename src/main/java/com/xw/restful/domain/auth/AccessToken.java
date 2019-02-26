package com.xw.restful.domain.auth;

import java.io.Serializable;

import com.xw.restful.utils.DateUtils;

public class AccessToken implements Cloneable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * accessToken
     */
    private String accessToken;

    /**
     * 创建时间，缓存失效机制用此时间来实现
     */
    private long createTime;

    /**
     * token过期剩余时间
     */
    private long expireIn;

    public AccessToken() {
        super();
    }

    public AccessToken(String accessToken, long createTime) {
        super();
        this.accessToken = accessToken;
        this.createTime = createTime;
    }

    public AccessToken(String accessToken) {
        super();
        this.accessToken = accessToken;
        this.createTime = DateUtils.getCurrentDateMilliSecond();
    }

    public long getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(long expireIn) {
		this.expireIn = expireIn;
	}

	/**
     * accessToken
     *
     * @return the accessToken
     */

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken
     *            the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * createTime
     *
     * @return the createTime
     */

    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
