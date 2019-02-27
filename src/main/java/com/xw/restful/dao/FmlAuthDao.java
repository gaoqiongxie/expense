package com.xw.restful.dao;

import org.apache.ibatis.annotations.Param;

import com.xw.restful.domain.FmlAuth;


public interface FmlAuthDao {

	FmlAuth getUserByNameAndPwd(@Param("uname")String uname, @Param("upwd")String upwd);

	int updateLastLoginTime(FmlAuth userInfo);

}
