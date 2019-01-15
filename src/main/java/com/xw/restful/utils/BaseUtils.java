package com.xw.restful.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public static List<Long> stringToLongList(String strArr) {
        return Arrays.stream(strArr.split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
    }
	
	public static List<Integer> stringToIntegerList(String strArr) {
        return Arrays.stream(strArr.split(","))
                        .map(s -> Integer.parseInt(s.trim()))
                        .collect(Collectors.toList());
    }

}
