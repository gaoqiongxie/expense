package com.xw.restful.utils;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.xw.restful.constant.ErrorCodeEnum;
import com.xw.restful.exceptions.BizException;
import com.xw.restful.stdo.APIRequest;



/**
 * 
 * <b>Description：</b> 参数类型转换 <br/>
 * <b>ClassName：</b> ParamDataEntity <br/>
 * <b>@author：</b> cheney CHU <br/>
 * <b>@date：</b> 2016年8月19日 上午9:54:08 <br/>
 * <b>@version: </b>  <br/>
 */
public class ParamDataEntity {
    private static final Logger logger = Logger.getLogger(ParamDataEntity.class);

    private Map<String,Object> paramList = new HashMap<String,Object>();
    private String paramData;
    
	public ParamDataEntity(Map<String, Object> paramList) {
        super();
        this.paramList = paramList;
    }

    public ParamDataEntity() {
        super();
    }
    
    public ParamDataEntity(APIRequest request) {
        this.paramList = request.getDataMap();
        this.paramData = request.getData();
    }

	
	public Map<String, Object> getParamList() {
		return paramList;
	}
	
	public void SetParamValue(String strParamName, Object objParamValue){
		if (StringUtils.isEmpty(strParamName)){
		    return;
		}
		//strParamName = strParamName.toUpperCase();
		paramList.put(strParamName, objParamValue);
	}
	
	public void RemoveParam(String strParamName){
		if (strParamName.length() == 0){
		    return;
		}
			
		strParamName = strParamName.toUpperCase();

		if (paramList != null){
			if (paramList.containsKey(strParamName)){
			    paramList.remove(strParamName);
			}
		}
	}
	
	public Object GetParamValue(String strParamName){
		//String strParamName2 = strParamName.toUpperCase();
		if (paramList.containsKey(strParamName)){
			Object objValue = paramList.get(strParamName);
			return objValue;
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * 获取非必填字段 <br/> 
	 * GetParamIntValue <br/> 
	 * @param strParamName
	 * @param nDefault
	 * @return  int <br/>
	 */
	public int GetParamIntValue(String strParamName, int nDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return nDefault;
		}
		try{
			return Integer.parseInt(objValue.toString());
		}catch (Exception ex){
			return nDefault;
		}
	}
	
	/**
	 * 
	 * 获取必填字段 <br/> 
	 * GetParamIntValueVerify <br/> 
	 * @param strParamName
	 * @return  int <br/>
	 */
	public int GetParamIntValue(String strParamName){
	    Object objValue = GetParamValue(strParamName);
        if (objValue == null){
        	 throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return Integer.parseInt(objValue.toString());
        }catch (Exception ex){
        	 throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
	}
	
	
	
	public float GetParamFloatValue(String strParamName, float fDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return fDefault;
		}
		try{
			return Float.parseFloat(objValue.toString());
		}
		catch (Exception ex){
			return fDefault;
		}
	}
	
	public float GetParamFloatValue(String strParamName){
	    Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return Float.parseFloat(objValue.toString());
        }
        catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
	}
	
	
	
	public BigDecimal GetParamBigDecimalValue(String strParamName, BigDecimal fDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return fDefault;
		}
		try{
			return BigDecimal.valueOf(Double.parseDouble(objValue.toString()));
		}
		catch (Exception ex){
			return fDefault;
		}
	}
	
	
	public BigDecimal GetParamBigDecimalValue(String strParamName){
	    Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return BigDecimal.valueOf(Double.parseDouble(objValue.toString()));
        }
        catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
	}
	
	
	
	public double GetParamDoubleValue(String strParamName, double fDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return fDefault;
		}
		try{
			return Double.parseDouble(objValue.toString());
		}catch (Exception ex){
			return fDefault;
		}
	}
	
   public double GetParamDoubleValue(String strParamName){
        Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return Double.parseDouble(objValue.toString());
        }catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
    }
   
	
	public long GetParamLongValue(String strParamName, long nDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return nDefault;
		}
		try{
			return Long.parseLong(objValue.toString());
		}catch (Exception ex){
			return nDefault;
		}
	}
	
	public long GetParamLongValue(String strParamName){
        Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return Long.parseLong(objValue.toString());
        }catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
    }
	
	
	public boolean GetParamBoolValue(String strParamName, boolean bDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return bDefault;
		}
		try{
			return Boolean.parseBoolean(objValue.toString());
		}catch (Exception ex){
			return bDefault;
		}
	}
	
	public boolean GetParamBoolValue(String strParamName){
        Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return Boolean.parseBoolean(objValue.toString());
        }catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
    }
	
	
	public String GetParamStringValue(String strParamName, String strDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return strDefault;
		}
		try{
			// /return objValue.toString().trim();
			return objValue.toString();
		}catch (Exception ex){
			return strDefault;
		}
	}
	
	public String GetParamStringValue(String strParamName){
        Object objValue = GetParamValue(strParamName);
        if (objValue == null){
            logger.info("获取参数失败，参数为空:"+strParamName);
            throw new BizException(ErrorCodeEnum.NULL_ERROR.getCode(), strParamName+ErrorCodeEnum.NULL_ERROR.getMsg());
        }
        try{
            return objValue.toString();
        }catch (Exception ex){
            throw new BizException(ErrorCodeEnum.PARAM_ERROR.getCode(), strParamName+ErrorCodeEnum.PARAM_ERROR.getMsg());
        }
    }
	
	public String GetParamClobValue(String strParamName, String strDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return strDefault;
		}
		try{
			if (objValue instanceof java.sql.Clob){
				boolean bFirst = true;
				Clob clob = (Clob) objValue;
				BufferedReader br = new BufferedReader(clob.getCharacterStream());
				String s = br.readLine();
				StringBuffer sb = new StringBuffer();
				while (s != null){// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
					if (bFirst)
						bFirst = false;
					else
						sb.append("\r\n");
					sb.append(s);
					s = br.readLine();
				}
				return sb.toString();
			}
			else
				return objValue.toString();
		}
		catch (Exception ex){
			return strDefault;
		}
	}
	public java.sql.Date GetParamDateValue(String strParamName, java.sql.Date dtDefault){
		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return dtDefault;
		}
		try{
			if (objValue instanceof java.sql.Date)
				return (java.sql.Date) objValue;
			if (objValue instanceof java.sql.Timestamp){
				java.sql.Timestamp ti = (java.sql.Timestamp) objValue;
				return new java.sql.Date(ti.getTime());
			}

			if (objValue instanceof java.util.Date){
				java.util.Date ti = (java.util.Date) objValue;
				return new java.sql.Date(ti.getTime());
			}
			
			return null;
		}catch (Exception ex){
			return dtDefault;
		}
	}
	
	public java.sql.Timestamp GetParamTimestampValue(String strParamName, java.sql.Timestamp dtDefault){

		Object objValue = GetParamValue(strParamName);
		if (objValue == null){
			return dtDefault;
		}
		try{
			if (objValue instanceof java.sql.Date){
				java.sql.Date date = (java.sql.Date)objValue;
				return new  java.sql.Timestamp(date.getTime()); 
			}
			
			if (objValue instanceof java.util.Date){
				java.util.Date date = (java.util.Date)objValue;
				return new  java.sql.Timestamp(date.getTime()); 
			}
			
			if (objValue instanceof java.sql.Timestamp){
				java.sql.Timestamp ti = (java.sql.Timestamp) objValue;
				return ti;
			}

			return null;
		}
		catch (Exception ex){
			return dtDefault;
		}
	}
	
	public <T> T getObject(Class<T> type){
	    return  (T) JSON.parseObject(this.paramData, type);
	}
	
}
