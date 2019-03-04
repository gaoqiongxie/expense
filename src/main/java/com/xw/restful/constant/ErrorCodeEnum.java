package com.xw.restful.constant;

public enum ErrorCodeEnum {
	SYSTEM_ERROR("0001", "系统错误"),
	NULL_ERROR("0002", "空异常"), 
	PARAM_ERROR("0003", "参数转换错误"),
	NO_ANTHORIZATION("0004", "没有权限"),
	NULL_ANTHORIZATION("0005", "token 空异常"),
	ERROR_LOGIN("0006", "账户或密码错误"),
	ERROR_USER_STATE("0007", "该用户无效"),
	UNLOGIN("0008", "用户未登录")
	;
	String code;
	String msg;

	ErrorCodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }



    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
