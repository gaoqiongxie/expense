package com.xw.restful.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

public class ReadExcelUtils {
	private String filePath;
	private Sheet sheet;
	private int startRowNum=1;
	private int endcolumnNum=100;
	private int sheetNum=1;
	private char suffix = '*';
	private List<String> columnHeaderList;
	private List<String[]> listData;
	private List<Map<String, String>> mapData;
	private InputStream inputStream;
	private String dataFormat = "yyyy-MM-dd HH:mm:ss";
	private boolean isInit = false;
	private DecimalFormat    df   ;   
	
	private static Logger logger = Logger.getLogger(ReadExcelUtils.class);

	public ReadExcelUtils(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * 通过指定行列读取单元格值
	 * @param row
	 * @param headerName
	 * @return
	 */
	public String getCellData(int row, String headerName)  {
		if(!isInit){
			init();
		}
		if (row <= 0) return null;
		
		if (mapData.size() >= row
				&& mapData.get(row - 1).containsKey(headerName)) {
			return mapData.get(row - 1).get(headerName);
		}
		
		return null;
	}
	
	/**
	 * 通过指定的列名读取单元格值
	 * @param headerNames
	 * @return
	 * @throws BizException
	 */
	public List<String[]> readExcel(String[] headerNames){
		if(!isInit) init();
		
		List<String[]> list = new ArrayList<String[]>();
		String headerName;
		// 列名重组
		headerNames = sortNewHeaderNames(headerNames);
		// 先判断列明是否全部存在 ，存在再解析，不存在返回异常信息
		checkColumnHeaderExist(columnHeaderList, headerNames);
		int rowEmptyCount;
		for (int i = 0; i < mapData.size(); i++) {
			rowEmptyCount = 0;
			String[] strs = new String[headerNames.length];
			for (int j = 0; j < headerNames.length; j++) {
				headerName = headerNames[j];
				strs[j] = mapData.get(i).get(headerName);
				if ("".equals(strs[j])) {
					rowEmptyCount++;
				}
			}
			// 当为空数量小于列明数量证明该数据是有效数据 ，进入list数组
			if (rowEmptyCount < headerNames.length) {
				list.add(strs);
			} else {
				return list;
			}
		}
		return list;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		FileInputStream inStream = null;
		try {
			//若是流为空，路径不为空
			if(inputStream==null&&!StringUtils.isEmpty(filePath)){
				inStream = new FileInputStream(new File(filePath));
			}else if(inputStream!=null&&StringUtils.isEmpty(filePath)){
				inStream =(FileInputStream) inputStream;
			}
			df =  new DecimalFormat("#0.00");
			Workbook workBook = WorkbookFactory.create(inStream);
		    sheet = workBook.getSheetAt(sheetNum-1);
			this.getSheetData();
			isInit = true;
		} catch (Exception e) {
			logger.error(e.toString(),e);
			logger.error("初始化失败，请检查文件路径与文件格式是否正确");
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 初始化sheet实体
	 */
	private void getSheetData() {
		
		listData = new ArrayList<String[]>();
		// 将每行的的数据以Map的Key键值处理，键值为列名，数值为单元格数值
		mapData = new ArrayList<Map<String, String>>();
		// 将第一行的列名形成数组
		columnHeaderList = new ArrayList<String>();
		int numOfRows = sheet.getLastRowNum() + 1;
		List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
		String cellvalue = null;
		Row firstRow=sheet.getRow(0);
		int numOfColumn = firstRow.getPhysicalNumberOfCells()<endcolumnNum ? firstRow.getPhysicalNumberOfCells() :endcolumnNum;
		for (int i = startRowNum-1; i < numOfRows; i++) {
			Row row = sheet.getRow(i);
			Map<String, String> map = new HashMap<String, String>();
			
			String[] list = new String[numOfColumn];
			if (row != null) {
				for (int j = 0; j < numOfColumn; j++) {
					Cell cell = row.getCell(j);
					cellvalue = this.getCombineCell(listCombineCell, cell,
							sheet);
					if(startRowNum==1){
						if (i == 0) {
							columnHeaderList.add(cellvalue);
						} else {
							map.put(columnHeaderList.get(j), cellvalue);
						}
					}
					
					list[j]=cellvalue;
				}
			}
			if (i > 0 && startRowNum==1) {
				mapData.add(map);
			}
			listData.add(list);
		}
	}
	
	/**
	 * 获取合并单元格
	 * @param sheet
	 * @return
	 */
	private  List<CellRangeAddress> getCombineCell(Sheet sheet) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格加入list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
		return list;
	}
	
	/**
	 * 获取合并单元格
	 * @param listCombineCell
	 * @param cell
	 * @param sheet
	 * @return
	 */
	private  String getCombineCell(
			List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		String cellValue = "";
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (null != cell && cell.getRowIndex() >= firstR
					&& cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC
						&& cell.getColumnIndex() <= lastC) {
					Row fRow = sheet.getRow(firstR);
					Cell fCell = fRow.getCell(firstC);
					cellValue = getCellValue(fCell);
					return cellValue;
				}
			}
		}
		cellValue = getCellValue(cell);
		return cellValue;
	}
	
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	private  String getCellValue(Cell cell) {
		String cellValue = "";

		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) { // 如果是时间类型
					SimpleDateFormat format = new SimpleDateFormat(dataFormat);
					cellValue = format.format(cell.getDateCellValue());
				} else { // 纯数字
					cellValue = df.format(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				if(cell.getCachedFormulaResultType() == 0){cellValue = df.format(cell.getNumericCellValue());};
				if(cell.getCachedFormulaResultType() == 1){cellValue = cell.getStringCellValue();}
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}

	/**
	 * 列头排序
	 * @param headerNames
	 * @return
	 */
	private String[] sortNewHeaderNames(String[] headerNames){
		// 非附加集合
		List<String> nomalHeaderNamesList = new ArrayList<String>();
		// 附加集合
		List<String> allLikeHeaderNameList = new ArrayList<String>();
		// 所有集合，最后先非附加集合和附加集合按顺序进入所有集合
		List<String> allHeaderNameList = new ArrayList<String>();
		// 列名
		String headerName = null;
		// 列名替换临时变量
		String headerNametemp = null;
		// Excel列名
		String excelheaderName = null;
		int headerNameLikeCount = 0;
		// 根据列名数组查询Excel匹配的列明
		for (int i = 0; i < headerNames.length; i++) {
			headerName = headerNames[i].trim();
			if (headerName.length() > 0) {
				// 区分是否为附加的列名
				if (headerName.charAt(headerName.length() - 1)!=suffix) {
					nomalHeaderNamesList.add(headerName);

				} else {
					headerNametemp = headerName.substring(0,
							headerName.length() - 1);
					headerNameLikeCount = 0;
					for (int j = 0; j < columnHeaderList.size(); j++) {
						excelheaderName = columnHeaderList.get(j);
						if (excelheaderName.indexOf(headerNametemp) == 0) {
							allLikeHeaderNameList.add(excelheaderName);
							headerNameLikeCount++;
						}
					}
					// 如果查询不到列名数组给出的列名报出异常
					if (headerNameLikeCount == 0) {
						logger.info("EXCLE_HEADER_EMPTY");
						return null;
					}
					headerNameLikeCount = 0;
				}
			}
			headerName = null;
		}
		// 列名数组重组，获取新的列名数组
		allHeaderNameList.addAll(nomalHeaderNamesList);
		allHeaderNameList.addAll(allLikeHeaderNameList);
		String[] newHeaderNames = new String[allHeaderNameList.size()];
		for (int i = 0; i < allHeaderNameList.size(); i++) {
			newHeaderNames[i] = allHeaderNameList.get(i);
		}
		return newHeaderNames;
	}
	
	/**
	 * 判断列头是否存在
	 * @param columnHeaderList
	 * @param checkHeaderNames
	 * @return
	 */
	private boolean checkColumnHeaderExist(List<String> columnHeaderList, String[] checkHeaderNames) {
		String headername = null;
		String checkHeaderName = null;
		Boolean isExsit = null;
		for (int i = 0; i < checkHeaderNames.length; i++) {
			checkHeaderName = checkHeaderNames[i];
			isExsit = false;
			for (int j = 0; j < columnHeaderList.size(); j++) {
				headername = columnHeaderList.get(j);
				if (headername.equals(checkHeaderName)) {
					isExsit = true;
					break;
				}
			}
			if (!isExsit) {
			    return false;
			}
		}
		return true;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public int getStartRowNum() {
		return startRowNum;
	}

	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}

	public int getEndcolumnNum() {
		return endcolumnNum;
	}

	public void setEndcolumnNum(int endcolumnNum) {
		this.endcolumnNum = endcolumnNum;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public char getSuffix() {
		return suffix;
	}

	public void setSuffix(char suffix) {
		this.suffix = suffix;
	}

	public List<String> getColumnHeaderList() {
		return columnHeaderList;
	}

	public void setColumnHeaderList(List<String> columnHeaderList) {
		this.columnHeaderList = columnHeaderList;
	}

	public List<String[]> getListData() {
		return listData;
	}

	public void setListData(List<String[]> listData) {
		this.listData = listData;
	}

	public List<Map<String, String>> getMapData() {
		return mapData;
	}

	public void setMapData(List<Map<String, String>> mapData) {
		this.mapData = mapData;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	public DecimalFormat getDf() {
		return df;
	}

	public void setDf(DecimalFormat df) {
		this.df = df;
	}
	
}
