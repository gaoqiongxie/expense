package com.xw.restful.utils.sce;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * ClassName: TokenProductFactory <br/>
 * Function: 生成Token 类 ，用于不同平台间安全验证<br/>
 * date: 2014-8-5 下午8:25:58 <br/>
 * 
 * @author laich
 */
public class TokenProductFactory {

    public final static String key="gzzyzz";
    private static TokenBaseInter base64 = new TokenToolEncrypterBase64();
    private static TokenBaseInter des64 = new TokenToolEncrypter();

    public static TokenBaseInter getInstallBase64(){
        return base64 ;
    }
    public static TokenBaseInter getInstallDES64(){
        return des64 ;
    }

    public static void main(String[] args) {
       /* long t1=System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            encodeStrSun("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123");
            decodeStrSun("ZWNmZGZmNzc3MDEzNDQyZTg1NTQwMDg1N2M3NDNjNTUjMjAzIzE2MWEzNzk3YzgwMzg5ZDZjMDkjQVQjJWV4ZyNMUXk_");
        }
        long t2=System.currentTimeMillis();
        System.out.println(t2-t1);
        
        
        long t3=System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            encodeStr("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123");
            decodeStr("ZWNmZGZmNzc3MDEzNDQyZTg1NTQwMDg1N2M3NDNjNTUjMjAzIzE2MWEzNzk3YzgwMzg5ZDZjMDkjQVQjJWV4ZyNMUXk_");
        }
        long t4=System.currentTimeMillis();
        System.out.println(t4-t3);
        
        
        long t5=System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            encodeStrXML("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123");
            decodeStrXML("ZWNmZGZmNzc3MDEzNDQyZTg1NTQwMDg1N2M3NDNjNTUjMjAzIzE2MWEzNzk3YzgwMzg5ZDZjMDkjQVQjJWV4ZyNMUXk_");
        }
        long t6=System.currentTimeMillis();
        System.out.println(t6-t5);*/
        
        
        
        System.out.println(base64.encrypt("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123"));
        System.out.println(encodeStrSun("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123"));
//        System.out.println(encodeStrXML("9cd5264f8227428f804a157fa68054c7#101#ret#at#123123123123"));
//        
        System.out.println(base64.decrypt("OWNkNTI2NGY4MjI3NDI4ZjgwNGExNTdmYTY4MDU0YzcjMTAxI3JldCNhdCMxMjMxMjMxMjMxMjM="));
        System.out.println(decodeStrSun("OWNkNTI2NGY4MjI3NDI4ZjgwNGExNTdmYTY4MDU0YzcjMTAxI3JldCNhdCMxMjMxMjMxMjMxMjM="));
//        System.out.println(decodeStrXML("OWNkNTI2NGY4MjI3NDI4ZjgwNGExNTdmYTY4MDU0YzcjMTAxI3JldCNhdCMxMjMxMjMxMjMxMjM="));
        
    }
    
   
    
    public static String decodeStrSun(String encodeStr){  
        byte[] b=null;
        try {
            b=new BASE64Decoder().decodeBuffer(encodeStr);
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return new String(b);  
    } 


    public static String encodeStrSun(String plainText){  
        return new BASE64Encoder().encode(plainText.getBytes());  
    } 

    

    public static String decodeStr(String encodeStr){  
        return new String(new Base64().decode(encodeStr.getBytes()));  
    } 


    public static String encodeStr(String plainText){  
        return new String(new Base64().encode(plainText.getBytes()));  
    } 
    
    
    public static String decodeStrXML(String encodeStr){  
        return new String(DatatypeConverter.parseBase64Binary(encodeStr));  
    } 


    public static String encodeStrXML(String plainText){  
        return new String(DatatypeConverter.printBase64Binary(plainText.getBytes()));  
    } 
}
