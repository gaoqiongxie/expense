package com.xw.restful.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {
	
	@SuppressWarnings("rawtypes")
    public static boolean isNullList(Object obj){
        boolean flag=true;
        if(obj instanceof List){
            List list=(List)obj;
            if(list!=null&&list.size()>0){
                flag=false;
            }
        }else if(obj instanceof Map){
            Map map=(Map)obj;
            if(map!=null&&map.size()>0){
                flag=false;
            }
        }else if(obj instanceof Set){
            Set set=(Set)obj;
            if(set!=null&&set.size()>0){
                flag=false;
            }
        }
        return flag;
    }
}
