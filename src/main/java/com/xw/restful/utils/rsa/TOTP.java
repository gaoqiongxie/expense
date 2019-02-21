package com.xw.restful.utils.rsa;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;

import com.xw.restful.constant.ErrorCodeEnum;

public class TOTP {

	/**
	 * 共享密钥
	 */
	private static final String SECRET_KEY = "ga35sdia43dhqj6k3f0la";

	/**
	 * 时间步长 单位:毫秒 作为口令变化的时间周期
	 */
	private static final long STEP = 30000;

	/**
	 * 初始化时间
	 */
	private static final long INITIAL_TIME = 0;
	
	/**
     * 数子量级
     */
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
    
    /**
     * 转码位数 [1-8]
     */
    private static final int CODE_DIGITS = 8;


	/**
	 * 生成一次性密码
	 *
	 * @param code
	 *            账户
	 * @param pass
	 *            密码
	 * @return String
	 */
	public static String generateMyTOTP(String code, String pass) {
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(pass)) {
			throw new RuntimeException(ErrorCodeEnum.NULL_ERROR.getMsg());
		}
		long now = System.currentTimeMillis();
		String time = Long.toHexString(timeFactor(now)).toUpperCase();
		return generateTOTP(code + pass + SECRET_KEY, time);
	}

	private static String generateTOTP(String key, String time) {
		return generateTOTP(key, time, "HmacSHA1");
	}

	private static String generateTOTP(String key, String time, String crypto) {
		StringBuilder timeBuilder = new StringBuilder(time);
		while (timeBuilder.length() < 16)
			timeBuilder.insert(0, "0");
		time = timeBuilder.toString();

		byte[] msg = hexStr2Bytes(time);
		byte[] k = key.getBytes();
		byte[] hash = hmac_sha(crypto, k, msg);
		return truncate(hash);
	}

	/**
     * 截断函数
     *
     * @param target 20字节的字符串
     * @return String
     */
    private static String truncate(byte[] target) {
        StringBuilder result;
        int offset = target[target.length - 1] & 0xf;
        int binary = ((target[offset] & 0x7f) << 24)
                | ((target[offset + 1] & 0xff) << 16)
                | ((target[offset + 2] & 0xff) << 8) | (target[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[CODE_DIGITS];
        result = new StringBuilder(Integer.toString(otp));
        while (result.length() < CODE_DIGITS) {
            result.insert(0, "0");
        }
        return result.toString();
    }

	/**
	 * 哈希加密
	 * @param crypto
	 * @param keyBytes
	 * @param text
	 * @return
	 */
    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "AES");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

	private static byte[] hexStr2Bytes(String hex) {
		byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
		byte[] ret = new byte[bArray.length - 1];
		System.arraycopy(bArray, 1, ret, 0, ret.length);
		return ret;
	}

	private static long timeFactor(long targetTime) {
		return (targetTime - INITIAL_TIME) / STEP;
	}

}
