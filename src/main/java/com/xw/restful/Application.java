package com.xw.restful;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动类
 * 
 * @author gaoqiongxie
 * @Date 2019年1月7日
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.xw.restful.dao"})
public class Application {
//	private static Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
