package com.xw.test.expense;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xw.restful.Application;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
@WebAppConfiguration
public class ExpenseApplicationTests {
	@Before
	public void init() {
		System.out.println("开始测试-----------------");
	}

	@After
	public void after() {
		System.out.println("测试结束-----------------");
	}
}
