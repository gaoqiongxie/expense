package com.xw.test.expense;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xw.restful.Application;
import com.xw.restful.stdo.APIRequest;
import com.xw.restful.stdo.APIResult;
import com.xw.restful.utils.BaseUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
@WebAppConfiguration
public class ExpenseApplicationTests {
	
	APIRequest apiRequest;
	APIResult apiResult;
	Map<String, Object> map;
	
	Object o;
	
	private void initDate() {
		apiRequest = new APIRequest();
		map = new HashMap<String, Object>();
		apiRequest.setDataMap(map);
		apiResult = new APIResult();
	}
	
	private void prinft() {
		System.out.println(BaseUtils.toJSONString(this.apiResult));
	}
	
	@Before
	public void init() {
		System.out.println("开始测试-----------------");
		initDate();
	}

	@After
	public void after() {
		apiResult.setData(o);
		prinft();
		System.out.println("测试结束-----------------");
	}
	
	public APIRequest getApiRequest() {
		return apiRequest;
	}

	public void setApiRequest(APIRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	public APIResult getApiResult() {
		return apiResult;
	}

	public void setApiResult(APIResult apiResult) {
		this.apiResult = apiResult;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
}
