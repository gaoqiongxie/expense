package com.xw.restful.utils.sce;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xw.restful.utils.StringUtil;


/**
 * ClassName: TokenToolEncrypter <br/>
 * Function: 一个Token加密与解密工具 ，不限于Token范围 <br/>
 * date: 2014-8-5 下午8:49:52 <br/>
 * <per> 使用时，结合 TokenProductFactory 生成 平台Token 通过解密，识别Token来源。 </per>
 * 
 * @author laich
 */
public class TokenToolEncrypterBase64 implements TokenBaseInter {

	private static final Log logger = LogFactory.getLog(TokenToolEncrypterBase64.class);
	

	/**
	 * 对字符串进行加密
	 * 
	 * @param str
	 * @return
	 */
	public String encrypt(String str) {
		return StringUtil.delBlank(encrypt(str.getBytes()).replace("=", "_"));
	}

	/**
	 * 对数组进行加密
	 * 
	 * @param str
	 * @return
	 */
	public String encrypt(byte[] b) {
		return Base64.encodeBase64String(b);//替换原有的BASE64Encoder
	}

	/**
	 * 对加密的信息进行解密
	 * 
	 * @param str
	 * @return
	 */
	public String decrypt(String str) {
			return new String(Base64.decodeBase64(str.replace("_", "="))); //替换原有的BASE64Encoder
	}

	/**
	 * 对加密的信息进行解密
	 * 
	 * @param str
	 * @return
	 */
	public String decrypt(byte[] b) {
		return decrypt(new String(b));
	}

	/* (non-Javadoc)
	 * @see wusc.edu.pay.common.utils.token.TokenBaseInter#productToken(java.lang.String[])
	 */
	@Override
	public String productToken(String[] pramaters) {
		if(pramaters==null || pramaters.length==0){
			return null;
		}else{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pramaters.length; i++) {
				sb.append(pramaters[i]+"-");
			}
			sb.append(key);//最后加上Key值
			return this.encrypt(sb.toString());
		}
		
	}

	/* (non-Javadoc)
	 * @see wusc.edu.pay.common.utils.token.TokenBaseInter#productToken(java.lang.String, java.lang.String)
	 */
	@Override
	public String productToken(String pix, String userNo) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("P_MEMBER_USER_" +"#");
	    sb.append(userNo +"#");
	    sb.append(System.currentTimeMillis()+"#");
	    sb.append(key);
	    System.out.println("加密前"+sb.toString());
		return this.encrypt(sb.toString());
	}

}
