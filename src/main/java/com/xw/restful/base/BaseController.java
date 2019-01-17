package com.xw.restful.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.xw.restful.stdo.APIRequest;
import com.xw.restful.utils.HttpRequestUtils;

public class BaseController {
	
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	
    /**
     * json check
     */
    private final static String LEFT_DKH = "{";

    private final static String RIGHT_DKH = "}";

	/**
	 * HttpServletRequest
	 */
	private HttpServletRequest request;

	/**
	 * HttpServletResponse
	 */
	private HttpServletResponse response;

	/**
	 * 设置request和response
	 * 
	 * @param request
	 * @param response
	 */
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * 获取request对象
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * 获取response对象
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return this.response;
	}

	protected APIRequest apiRequest;

	public APIRequest getApiRequest() {
		return apiRequest;
	}

	public void initParams() {
		try {
			String submitMehtod = request.getMethod();
			// GET
			if (submitMehtod.equals("GET")) {
				String queryString = request.getQueryString();

				if (isJsonStr(queryString)) {// json格式
					queryString = queryString == null ? "{}" : urlDecode(queryString);
					apiRequest = JSON.parseObject(queryString, APIRequest.class);
				} else {// 普通参数
					Map properties = HttpRequestUtils.getRequestParamters(request);
					if (properties == null) {
						properties = new HashMap<>();
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("source", properties.get("source"));
					paramMap.put("time", properties.get("time"));
					properties.remove("source");
					properties.remove("time");
					paramMap.put("data", properties);
					apiRequest = JSON.parseObject(JSON.toJSONString(paramMap), APIRequest.class);
				}
			} else {// POST|PUT|DELETE
				String postJsonStr = HttpRequestUtils.getRequestPostStr(request);
				if (isJsonStr(postJsonStr)) {// JSON格式
					apiRequest = JSON.parseObject(postJsonStr, APIRequest.class);
				} else {
					Map properties = HttpRequestUtils.getRequestParamters(request);
					if (properties == null) {
						properties = new HashMap<>();
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("source", properties.get("source"));
					paramMap.put("time", properties.get("time"));
					properties.remove("source");
					properties.remove("time");
					paramMap.put("data", properties);
					apiRequest = JSON.parseObject(JSON.toJSONString(paramMap), APIRequest.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String urlDecode(String input) {
		try {
            return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
	}

	private boolean isJsonStr(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return false;
		}
		if (jsonStr.startsWith(LEFT_DKH) && jsonStr.endsWith(RIGHT_DKH)) {
			return true;
		}
		return false;
	}
	
}
