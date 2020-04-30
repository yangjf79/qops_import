package com.nvent.qops.entity;

import java.util.Arrays;
import java.util.List;

/*
 * 1，根据 nickname确定需要导入那个excel文件
 * 2，excelLocation + excelName 拼接找到 Excel文件
 * 3，sheetName 是导入excel中的那个表
 * 4，大约有10多个表需要导入该系统的数据库中，需要做定时任务
 * 5，每个表的定时任务不同，大体上为每天的 5：10 第一次，然后 8：10 ~ 16：10 每隔小时一次
 * 6，由于ERP系统不稳定，需要设置专人手动也可以导入的功能
 * */
public class ExcelData {
	private int id;
	private String nickname;
	private String excelLocation;
	private String excelName;
	private String sheetName;
	private String pkColumns;
	private String updateColumns;
	
	private String[] pkCols;
	private List<String> updateCols;

	public String getPkColumns() {
		return pkColumns;
	}

	public void setPkColumns(String pkColumns) {
		this.pkColumns = pkColumns;
		if (pkColumns != null && pkColumns.length() > 0) {
			this.pkCols = pkColumns.split(",");
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getExcelLocation() {
		return excelLocation;
	}

	public void setExcelLocation(String excelLocation) {
		this.excelLocation = excelLocation;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getPkCols() {
		return pkCols;
	}

	public void setPkCols(String[] pkCols) {
		this.pkCols = pkCols;
	}

	public String getUpdateColumns() {
		return updateColumns;
	}

	public void setUpdateColumns(String updateColumns) {
		this.updateColumns = updateColumns;
		if (updateColumns != null && updateColumns.length() > 0) {
			String[] cols = updateColumns.split(",");
			this.updateCols = Arrays.asList(cols);
		}
	}

	public List<String> getUpdateCols() {
		return updateCols;
	}

	public void setUpdateCols(List<String> updateCols) {
		this.updateCols = updateCols;
	}

}
