package com.xw.restful.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

public class HttpRequestUtils {
	
	/**
	 *  获取所有的请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getRequestParamters(HttpServletRequest request) {
		// 获取所有的请求参数
		Map properties = new HashMap(request.getParameterMap());
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
    private static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
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
