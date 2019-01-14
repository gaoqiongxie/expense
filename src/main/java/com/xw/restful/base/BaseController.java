package com.xw.restful.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.xw.restful.stdo.APIRequest;

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
//		APIRequest apirequest = null;
		try {
			String submitMehtod = request.getMethod();
			// GET
			if (submitMehtod.equals("GET")) {
				String queryString = request.getQueryString();

				if (isJsonStr(queryString)) {// json格式
					queryString = queryString == null ? "{}" : urlDecode(queryString);
					apiRequest = JSON.parseObject(queryString, APIRequest.class);
				} else {// 普通参数
					Map properties = getRequestParamters(request);
					if (properties == null) {
						properties = new HashMap<>();
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("source", properties.get("source"));
					paramMap.put("time", properties.get("time"));
					paramMap.put("wx_openID", properties.get("wx_openID"));
					paramMap.put("opTicket", properties.get("opTicket"));
					properties.remove("source");
					properties.remove("time");
					properties.remove("wx_openID");
					properties.remove("opTicket");
					paramMap.put("data", properties);
					apiRequest = JSON.parseObject(JSON.toJSONString(paramMap), APIRequest.class);
				}
			} else {// POST|PUT|DELETE
				String postJsonStr = getRequestPostStr(request);
				if (isJsonStr(postJsonStr)) {// JSON格式
					apiRequest = JSON.parseObject(postJsonStr, APIRequest.class);
				} else {
					Map properties = getRequestParamters(request);
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

	/**
	 * 获得所有请求的参数
	 * 
	 * @param request
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getRequestParamters(HttpServletRequest request) {
		// 获取所有的请求参数
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		// 读取map中的值
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = " ";
				returnMap = JSON.parseObject(name);
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (String value2 : values) {
					value = value2 + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			// 将读取到的值存入map中
			returnMap.put(name, value);
		}
		return returnMap;
	}

	/**
	 * 描述:获取 post 请求内容
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestPostStr(HttpServletRequest request) throws IOException {
		byte buffer[] = getRequestPostBytes(request);
		if (buffer == null) {
			return null;
		}

		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}
	
	/**
     * 描述:获取 post 请求的 byte[] 数组
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }
}
