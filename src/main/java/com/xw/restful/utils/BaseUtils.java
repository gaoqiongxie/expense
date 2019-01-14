package com.xw.restful.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;



public class BaseUtils {
	
	public static String toJSONString(Object obj){
		return JSONObject.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
	}

	public static int getPage(int pageSize,int page){
        page = page>0?page:1;
        return (page-1)*pageSize;
    }
}
