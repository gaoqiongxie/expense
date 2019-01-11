package com.xw.test.hello;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xw.hello.HelloWorldController;

public class HelloWorldControllerTest {
	@Test
	public void testSayHello() {
		assertEquals("Hello,World!", new HelloWorldController().sayHello());
	}
}
