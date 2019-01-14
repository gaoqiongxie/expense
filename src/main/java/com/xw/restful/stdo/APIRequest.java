/**
* 上海轩言网络信息科技有限公司
* Copyright (c) 2016, xuanyan All Rights Reserved.
*/
package com.xw.restful.stdo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.DateUtil;
import org.omg.CORBA.Current;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * <b>Description：</b> 请求数据包装类 <br/>
 * <b>ClassName：</b> RequestWrapper <br/>
 * <b>@author：</b> jackyshang <br/>
 * <b>@date：</b> 2016年7月12日 上午11:22:18 <br/>
 * <b>@version: </b> <br/>
 */
public class APIRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 
     * 请求来源 
     */
    private String source;

    /** 
     * 发起请求时间戳</br>
     * 格式：timestamp
     */
    private String time;

    /** 
     * 请求数据
     */
    private String data;
    
    /**
     * map类型的数据
     */
    private Map<String,Object> dataMap;
    /**
     * 客户端请求IP
     */
    private String clientIP;
    /**
     * 请求协议
     */
    private String schema;


    public APIRequest() {
        this.time = String.valueOf(System.currentTimeMillis());
    }

    public APIRequest(String data) {
        super();
        this.data = data;
        this.dataMap = JSON.parseObject(data);
        this.time = String.valueOf(System.currentTimeMillis());
    }
    
    public APIRequest(String source, String data) {
        super();
        this.source = source;
        this.dataMap = JSON.parseObject(data);
        this.data = data;
        this.time = String.valueOf(System.currentTimeMillis());
    }
    public APIRequest(String source,String time, String data) {
        super();
        this.source = source;
        this.dataMap = JSON.parseObject(data);
        this.data = data;
        this.time = time;
    }
    

	public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.dataMap = JSON.parseObject(data);
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        
        this.data = JSON.toJSONString( this.dataMap);
    }
    /**
     * 向datamap中设置值
     * putData <br/> 
     * @param k
     * @param v  void <br/>
     */
   public void put(String key, Object value){
       if( this.dataMap==null){
           this.dataMap = new HashMap<String, Object>();
       }
       this.dataMap.put(key, value);
       this.data = JSON.toJSONString(this.dataMap);
    }
    


	/**
     * clientIP
     *
     * @return  the clientIP
     */
    
    public String getClientIP() {
        return clientIP;
    }

    /**
     * @param clientIP the clientIP to set
     */
    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
    
    public String getSchema() {
        return schema;
    }
    
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
    
    
}
