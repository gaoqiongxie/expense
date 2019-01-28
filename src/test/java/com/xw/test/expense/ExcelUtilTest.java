package com.xw.test.expense;

import java.util.List;

import org.junit.Test;

import com.xw.restful.utils.excel.ReadExcelUtils;


public class ExcelUtilTest{

	@Test
	public void testRead() {

		ReadExcelUtils eh = new ReadExcelUtils("f://excel/fml_expense_1548654298596.xlsx");
//		eh.setEndcolumnNum(3);
		//支出人	支付人	支出金额	支出类型	支出时间	备注
		
		System.out.println("-----------------通过指定行列读取单元格值----------------------------------");
		System.out.println(eh.getCellData(2,"支出人"));
		
		System.out.println("-------------------通过指定的列名读取单元格值-------------------------------");
		String[] HeaderNanames = { "支出人", "支付人", "支出金额","支出类型","支出时间","备注"};
		List<String[]> list = eh.readExcel(HeaderNanames);
        //List<String[]> list = eh.readExcel();
		String[] strs = null;
		for (int i = 0; i < list.size(); i++) {
			strs = (String[]) list.get(i);
			for (int j = 0; j < strs.length; j++) {
				System.out.println("序列" + i + ":" + strs[j]);
			}
		}
	}
}
