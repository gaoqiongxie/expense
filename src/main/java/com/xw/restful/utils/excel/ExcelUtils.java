package com.xw.restful.utils.excel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xw.restful.anotation.ExcelExport;

public class ExcelUtils {
	private static Logger logger = Logger.getLogger(ExcelUtils.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static <T> XSSFWorkbook producedExcel(String sheetName,List<T> dataList,String config) {
		if(StringUtils.isEmpty(sheetName) || dataList==null || dataList.size()==0){
			return null;
		}
		try {
			List<String> headerValues = new ArrayList<String>();
			List<String> headerValusNickNames = new ArrayList<String>();
			Map<String, Integer> colWidthsMap = new HashMap<String,Integer>();
			if(StringUtils.isEmpty(config)){
				initProperty(dataList.get(0),headerValues,headerValusNickNames,colWidthsMap);
			}else{
				initProperty(config,headerValues,headerValusNickNames,colWidthsMap);
			}
			XSSFWorkbook xSSFWorkbook = createWorkbook(sheetName, headerValusNickNames, colWidthsMap);
			wirte(dataList,xSSFWorkbook, headerValues);
			
			return xSSFWorkbook;
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return null;
		}
	}

	/**
	 * 创建XSSFWorkbook并且输入标题行
	 * @param sheetName
	 * @param headerValusNickNames
	 * @param colWidthsMap
	 * @return
	 */
	public static XSSFWorkbook createWorkbook(String sheetName,List<String> headerValusNickNames,Map<String, Integer> colWidthsMap) {
        XSSFWorkbook sfwb =new XSSFWorkbook();
		 
	    //拿到excel中的第一个sheet
	    XSSFSheet sheet=sfwb.createSheet(sheetName);
	    //拿到第一行
	    XSSFRow rowOne =sheet.createRow(0);
	    //设置标题样式
	    XSSFCellStyle headStyle = sfwb.createCellStyle();
	    setCellStyle(headStyle,"head",true);
	 
	    for (int i = 0; i < headerValusNickNames.size(); i++) {
		   XSSFCell cell =rowOne.createCell(i);
		   cell.setCellValue(headerValusNickNames.get(i));
		   cell.setCellStyle(headStyle);
		   sheet.setColumnWidth(i, (colWidthsMap.get(headerValusNickNames.get(i)).intValue())*256);
	    }
	 
	    return sfwb;
	}

	/**
	 * 单元格样式
	 * @param cellStyle
	 * @param type
	 * @param isSimpleStyle
	 */
	private static void setCellStyle(CellStyle cellStyle,String type,boolean isSimpleStyle){
		switch(type){
		case "head":
			if(!isSimpleStyle){//暂且设置样式无用
				//上边框
				cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
				//下边框     
				cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				//左边框   
				cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);	
				//右边框    
				cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);	
			    cellStyle.setFillForegroundColor((short) 13);								
			    cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				cellStyle.setVerticalAlignment(XSSFCellStyle.SOLID_FOREGROUND);
				cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			}
			
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
			break;
		default: 
		    cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
		    break;
		
		}
	}

	/**
	 * 通过配置文件初始化
	 * @param config
	 * @param headerValues
	 * @param headerValusNickNames
	 * @param colWidthsMap
	 */
	public static <T> void initProperty(String config,List<String> headerValues,List<String> headerValusNickNames,Map<String, Integer> colWidthsMap){
		String[] headers =config.split(",");
		 for (int i = 0; i < headers.length; i++) {
			 //解析 {"学生姓名","userName","15"}
			String[] headerNames =headers[i].split(":");
			headerValues.add(headerNames[1]);
			headerValusNickNames.add(headerNames[0]);
			colWidthsMap.put(headerNames[0], Integer.parseInt(headerNames[2]));
		}
		
	}

	/**
	 * 往excel写内容
	 * @param dataList
	 * @param xSSFWorkbook
	 * @param headerValues
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> void wirte(List<T> dataList,XSSFWorkbook xSSFWorkbook,List<String> headerValues){
		// 设置内容样式
	    XSSFCellStyle contentStyle = xSSFWorkbook.createCellStyle(); 
	    setCellStyle(contentStyle,"content",true);
	    
	    boolean isMap = false;
	    if(dataList.get(0) instanceof Map){
	    	isMap = true;
	  	}
		
		XSSFSheet sheet = xSSFWorkbook.getSheetAt(0);
		for (int i = 0; i < dataList.size(); i++) {
			Object classObject=dataList.get(i);
			XSSFRow row=sheet.createRow(i+1);
			for (int j = 0; j < headerValues.size(); j++) {
				
				Method method=null;
				Object object=null;
				XSSFCell cell=row.createCell(j);
				cell.setCellStyle(contentStyle);
                if(isMap){
                	object = ((Map<String,Object>)classObject).get(headerValues.get(j));
				}else{
					try {
						method=classObject.getClass().getMethod("get"+StringUtils.capitalize(headerValues.get(j)));
						object=method.invoke(classObject);
					} catch (Exception e) {
						logger.error(e.toString(),e);
					}
				}
				
				if(object==null){
					object="";
				}else if(object instanceof Date){
					object=sdf.format(object);
				}
				cell.setCellValue(object.toString());
			}
		}
	}

	/**
	 * 通过实体初始化
	 * @param t
	 * @param headerValues
	 * @param headerValusNickNames
	 * @param colWidthsMap
	 */
	public static <T> void initProperty(T t,List<String> headerValues,List<String> headerValusNickNames,Map<String, Integer> colWidthsMap){
		List<Integer> sorts = new ArrayList<Integer>();
		Map<Integer,String> sortHeaderValues = new HashMap<Integer,String>();
		Map<Integer,String> sortHeaderValuesNickNames = new HashMap<Integer,String>();
		
		List<Field>  fields = getFields(t);
		for(Field field : fields){
			if (field.isAnnotationPresent(ExcelExport.class)) {
				field.setAccessible(true);
				ExcelExport excelExport = field.getAnnotation(ExcelExport.class);
				String nickname = excelExport.nickname();
				int sort = excelExport.sort();
				Integer columnWidths= excelExport.columnWidths();
				String name = field.getName();
				colWidthsMap.put(nickname, columnWidths);
				sorts.add(sort);
				sortHeaderValues.put(sort, name);
				sortHeaderValuesNickNames.put(sort,nickname);
				field.setAccessible(false);
			}
		}
		
		Collections.sort(sorts);
		
		for(Integer temp : sorts){
			headerValues.add(sortHeaderValues.get(temp));
			headerValusNickNames.add(sortHeaderValuesNickNames.get(temp));
		}
		
		
	}
	
	/**
	 * 获取bean的所有field(包含继承类的私有field)
	 * @param t
	 * @return
	 */
	public static <T> List<Field> getFields(T t){
		Class<?> classInfo = t.getClass();
		List<Field> fieldList = new ArrayList<Field>();
		fieldList.addAll(Arrays.asList(classInfo.getDeclaredFields()));
		while((classInfo= classInfo.getSuperclass()) != Object.class){
			Field[] fields = classInfo.getDeclaredFields();
			fieldList.addAll(Arrays.asList(fields));
		}
		return fieldList;
	}

	/**
	 * 输出excel
	 * @param response
	 * @param workbook
	 * @param fileName
	 * @throws IOException
	 */
	public static void renderExcel(HttpServletResponse response,Workbook workbook,String fileName) throws IOException{
        response.setContentType("application/x-excel");
        response.setCharacterEncoding("UTF-8"); 
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

}
