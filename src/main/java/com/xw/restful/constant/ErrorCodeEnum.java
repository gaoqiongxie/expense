package com.xw.restful.constant;

public enum ErrorCodeEnum {
	SYSTEM_ERROR("0001","系统错误"),
	NULL_ERROR("0002","空异常"), 
	PARAM_ERROR("0003","参数转换错误"),
	NO_ANTHORIZATION("0004","没有权限"),
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
