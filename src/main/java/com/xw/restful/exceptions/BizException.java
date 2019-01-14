package com.xw.restful.exceptions;

import com.xw.restful.constant.ErrorCodeEnum;


/**
 * 
 * <b>Description：</b> 自定义业务异常 <br/>
 * <b>ClassName：</b> DALException <br/>
 * <b>@author：</b> jackyshang <br/>
 * <b>@date：</b> 2016年7月12日 上午9:51:19 <br/>
 * <b>@version: </b>  <br/>
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = -5875371379845226068L;


	/**
	 * 异常信息
	 */
	protected String msg;

	/**
	 * 具体异常码
	 */
	protected String code;

	public BizException(String code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
		this.msg = String.format(msgFormat, args);
	}
	

	public BizException() {
		super();
	}

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public  BizException newInstance(String msgFormat, Object... args) {
		return new BizException(this.code, msgFormat, args);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message) {
		super(message);
	}

}
