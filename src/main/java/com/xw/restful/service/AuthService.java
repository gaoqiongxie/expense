package com.xw.restful.service;

import com.xw.restful.stdo.APIRequest;

public interface AuthService {

	Object login(APIRequest apiRequest);

	Object logout(APIRequest apiRequest);

	boolean validateAccessToken(String accessToken);

}
