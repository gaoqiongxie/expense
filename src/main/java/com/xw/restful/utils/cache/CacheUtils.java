package com.xw.restful.utils.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class CacheUtils {
	
	private static Logger logger = Logger.getLogger(CacheUtils.class);
	
	@SuppressWarnings("rawtypes")
	private static Map<String, CacheData> CACHE_DATA = new ConcurrentHashMap<>();
	
	public static <T> boolean containsKey(String key){
		return CACHE_DATA.containsKey(key);
	}
	
	@SuppressWarnings("rawtypes")
	public static <T> boolean containsValue(String value){
		boolean containsFlag = false;
		
		for (Entry<String, CacheData> entry : CACHE_DATA.entrySet()) {
			CacheData data = entry.getValue();
			if(data != null && (data.getExpire() <= 0 || data.getSaveTime() >= System.currentTimeMillis()) 
					&& data.data.equals(value)){
				containsFlag = true;
	        }
        }
		return containsFlag;
	}
	
    public static <T> T getData(String key,Load<T> load,int expire){
        T data = getData(key);
        if(data == null && load != null){
            data = load.load();
            if(data != null){
                setData(key,data,expire);
            }
        }
        return data;
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getData(String key){
        CacheData<T> data = CACHE_DATA.get(key);
        if(data != null && (data.getExpire() <= 0 || data.getSaveTime() >= System.currentTimeMillis())){
            return data.getData();
        }
        return null;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void setData(String key,T data,int expire){
        CACHE_DATA.put(key,new CacheData(data,expire));
    }
    
  	@SuppressWarnings("rawtypes")
	public static void clearByToken(String value){
		for (Entry<String, CacheData> entry : CACHE_DATA.entrySet()) {
			CacheData data = entry.getValue();
			if(data != null && (data.getExpire() <= 0 || data.getSaveTime() >= System.currentTimeMillis()) 
					&& data.data.equals(value)){
				clearByKey(entry.getKey());
			}
		}
      	
    }
    
    public static void clearByKey(String key){
    	CACHE_DATA.remove(key);
    }
    
    public static void clearAll(){
        CACHE_DATA.clear();
    }
    
    @SuppressWarnings("rawtypes")
	public static void printf() {
    	for (Entry<String, CacheData> entry : CACHE_DATA.entrySet()) {
			logger.info("CACHE_DATA: key-"+entry.getKey()+"  value-"+entry.getValue().toString());
        }
    }
    
    public interface Load<T>{
        T load();
    }
    
    private static class CacheData<T>{
        CacheData(T t,int expire){
            this.data = t;
            this.expire = expire <= 0 ? 0 : expire * 1000;
            this.saveTime = System.currentTimeMillis() + this.expire;
        }
        private T data;
        private long saveTime; // 存活时间
        private long expire;   // 过期时间 小于等于0标识永久存活
        public T getData() {
            return data;
        }
        public long getExpire() {
            return expire;
        }
        public long getSaveTime() {
            return saveTime;
        }
		@Override
		public String toString() {
			return "CacheData [data=" + data + ", saveTime=" + saveTime + ", expire=" + expire + "]";
		}
        
    }

}
