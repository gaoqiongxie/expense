package com.xw.test.expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xw.restful.dao.FmlExpenseDao;
import com.xw.restful.domain.FmlExpense;
import com.xw.restful.utils.excel.ReadExcelUtils;

public class ExcelUtilTest extends ExpenseApplicationTests {
	
	@Autowired
	FmlExpenseDao fmlExpenseDao;

	@Test
	public void testRead() {
		ReadExcelUtils eh = new ReadExcelUtils("f://excel/fml_expense_1548654298596.xlsx");
		// eh.setEndcolumnNum(3);
		// eh.setStartRowNum(100);
		// 支出人 支付人 支出金额 支出类型 支出时间 备注

		System.out.println("-----------------通过指定行列读取单元格值----------------------------------");
		System.out.println(eh.getCellData(2, "支出人"));

		System.out.println("-------------------通过指定的列名读取单元格值-------------------------------");
		String[] HeaderNanames = { "支出人", "支付人", "支出金额", "支出类型", "支出时间", "备注" };
		List<String[]> list = eh.readExcel(HeaderNanames);
		// List<String[]> list = eh.readExcel();
		String[] strs = null;
		for (int i = 1; i < list.size(); i++) {
			strs = (String[]) list.get(i);
			for (int j = 0; j < strs.length; j++) {
				System.out.println("row" + i + "-col" + j + ":" + strs[j]);
			}

		}
	}

	@Test
	public void importExpenseData() {

		try {
			ReadExcelUtils eh = new ReadExcelUtils("f://excel/fml_expense_1548654298596.xlsx");

			System.out.println("-------------------通过指定的列名读取单元格值-------------------------------");
			List<String[]> list = eh.readExcel();
			
			String[] strs = null;
			FmlExpense e = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<FmlExpense> fmlEList = new ArrayList<FmlExpense>();
			
			for (int i = 1; i < list.size(); i++) {
				strs = (String[]) list.get(i);
				e = new FmlExpense();
				e.setExpenseId(parseRole(strs[0]));
				e.setPayerId(parseRole(strs[1]));
				e.setExpense(Float.parseFloat(strs[2]));
				e.setExpenseType(parseType(strs[3]));
				e.setExpenseTime(sdf.parse(strs[4]));
				e.setExpenseDesc(strs[5]);

				System.out.println(e.toString());
				fmlEList.add(e);
			}
			
//			fmlExpenseDao.insertBatch(fmlEList);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String parseType(String typeName) {
		String typeId = "0";
		// 1-交通
		String[] typeNames = { "交通", "生活用品", "服饰美容", "饮食", "住房缴费", "文教娱乐", "运动健康", "通讯物流", "医疗", "其他" };
		for (int i = 0; i < typeNames.length; i++) {
			if (typeName.equals(typeNames[i])) {
				typeId = String.valueOf(i + 1);
				break;
			}
		}
		return typeId;
	}

	private int parseRole(String roleName) {
		int roleId = 0;
		String[] roleNames = { "滚滚", "麻麻", "爸爸", "家庭", "16", "其他", "两只", "三只" };
		for (int i = 0; i < roleNames.length; i++) {
			if (roleName.equals(roleNames[i])) {
				roleId = i + 1;
				break;
			}
		}
		return roleId;
	}
}
