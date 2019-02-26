package com.xw.restful.utils.auth;

import com.xw.restful.utils.UUIDUitl;
import com.xw.restful.utils.sce.TokenProductFactory;

public class AuthUtil {

	// 加密字段的分割符
	private static final String EncrypterSplit = "#";
	// key的分割符
//	private static final String KEY_SPLIT = "_";
	// accessToken标记
	private static final String KEY_ACCESS_TOKEN = "AT";
	// refreshToken标记
	private static final String KEY_REFRESH_TOKEN = "RT";

	public static String getRefreshToken(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(userId + EncrypterSplit);
		sb.append(KEY_REFRESH_TOKEN + EncrypterSplit);
		sb.append(UUIDUitl.generateAllString(8));
		return TokenProductFactory.getInstallBase64().encrypt(sb.toString());
	}

	public static String getAccessToken(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(userId + EncrypterSplit);
		sb.append(KEY_ACCESS_TOKEN + EncrypterSplit);
		sb.append(UUIDUitl.generateAllString(8));

		return TokenProductFactory.getInstallBase64().encrypt(sb.toString());
	}

	public static String getUserIdByToken(String token) {
		String[] tokenItemArray = TokenProductFactory.getInstallBase64().decrypt(token).split(EncrypterSplit);
		return tokenItemArray[0];
	}
}
