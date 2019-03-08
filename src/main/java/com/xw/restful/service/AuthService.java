package com.xw.restful.service;

import com.xw.restful.domain.auth.TokenModel;
import com.xw.restful.domain.auth.UserAuth;
import com.xw.restful.stdo.APIRequest;

public interface AuthService {

	TokenModel login(APIRequest apiRequest);

	void logout(APIRequest apiRequest);

	boolean validateAccessToken(String accessToken);

	UserAuth refresh(String refreshToken);

}
