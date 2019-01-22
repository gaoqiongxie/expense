package com.xw.test.expense;

import org.apache.log4j.Logger;

public class Log4JTest {
	private static Logger logger = Logger.getLogger(Log4JTest.class); // 获取logger实例

	public static void main(String[] args) {
		logger.info("普通Info信息");
		logger.debug("调试debug信息");
		logger.error("错误error信息");
		logger.warn("警告warn信息");
		logger.fatal("严重错误fatal信息");

		// 开发中有可能会遇到一下经典异常
		logger.error("错误了", new IllegalArgumentException("非法参数异常"));
	}
}
