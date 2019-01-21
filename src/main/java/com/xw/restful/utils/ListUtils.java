package com.xw.restful.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xw.restful.domain.vo.MonthExpense;


public class ListUtils {

	public static List mapToList(Map<String, Map<String, Map<String, Map<String, List<MonthExpense>>>>> groupBy) {
		List relist = new ArrayList();
		groupBy.forEach((k, v) -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", k);//月
			map.put("children", getPayChild(v));
			relist.add(map);
		});
		
		return relist;
	}

	private static Object getPayChild(Map<String, Map<String, Map<String, List<MonthExpense>>>> payV) {//支付人
		List relist = new ArrayList();
		payV.forEach((k, v) -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", k);//支付人
			map.put("children", getExpenseChild(v));
			relist.add(map);
		});
		
		return relist;
	}

	private static Object getExpenseChild(Map<String, Map<String, List<MonthExpense>>> expenseV) {
		List relist = new ArrayList();
		expenseV.forEach((k, v) -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", k);//支出人
			map.put("children", getTypeChild(v));
			relist.add(map);
		});
		
		return relist;
	}

	private static Object getTypeChild(Map<String, List<MonthExpense>> typeV) {
		List relist = new ArrayList();
		typeV.forEach((k, v) -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", k);//支出类型
			map.put("value", getDetail(k,v));
			relist.add(map);
		});
		
		return relist;
	}

	private static Object getDetail(String k, List<MonthExpense> dv) {
		for (MonthExpense v : dv) {
			if(k.equals(v.getTypeName())) return v.getExpense();
		}
		return 0;
	}

}
