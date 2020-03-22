package com.nvent.qops.module;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nvent.qops.dao.ExcelDataDAO;
//import com.nvent.qops.eco.module.ExcelEcoModule;
import com.nvent.qops.entity.ExcelData;
//import com.nvent.qops.so.module.ExcelSoModule;
import com.nvent.util.ReadExcelUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ExcelDataModule {
	public static final Logger log = LoggerFactory.getLogger(ExcelDataModule.class);

	@Autowired
	private ExcelDataDAO excelDataDAO;

//	@Autowired
//	private ExcelEcoModule excelEcoModule;
	
//	@Autowired
//	private ExcelSoModule excelSoModule;
	
	@Value("${qopsURL:http://127.0.0.1:8080}")
	private String qopsURL;
	
	@PostConstruct
	private void init() {
		//清理 excel_eco 表中的重复数据
		clearDuplicateRecordsInExcelEco();
	}
	
	public List<ExcelData> getAllExcelData() {
		return excelDataDAO.getAllExcelData();
	}

	public ExcelData getExcelDataById(int id) {
		return excelDataDAO.getExcelDataById(id);
	}

	public boolean saveExcelData(ExcelData data) {
		if (data.getId() == -1) {
			excelDataDAO.createExcelData(data);
		} else {
			excelDataDAO.updateExcelData(data);
		}
		return true;
	}

	public boolean deleteExcelData(int id) {
		excelDataDAO.deleteExcelData(id);
		return true;
	}
	
	//Import excel data to mySql by manual
	public void excelImportManual(ExcelData excelData) {
		doExcelImport(excelData);
	}
	
	@Scheduled(cron = "${excelImport.cron}")
	public void excelImportTask() {
		log.info("Begin to execute excel import.");

		// 遍历并导入Excel文件
		List<ExcelData> excels = excelDataDAO.getAllExcelData();

		for (ExcelData excel : excels) {
			//log.info(excel.getNickname());
			//1. 导入外部表格数据
			doExcelImport(excel);
			
			//2. eco 导入完成后，匹配 ECO_No
			if(excel.getNickname().equals("eco")) {
				//excelEcoModule.saveEcoToRfqDetailAndCreatEco();
				try {
					OkHttpClient client = new OkHttpClient();

					Request request = new Request.Builder()
							.url(qopsURL + "/api/eco/saveEcoToRfqDetailAndCreatEco")
							.get().addHeader("cache-control", "no-cache").build();

					Response response = client.newCall(request).execute();
				} catch (IOException e) {
					log.error("saveEcoToRfqDetailAndCreatEco error", e);
				}
			}
			//3. so 导入完成后，匹配 SO_NO
			if(excel.getNickname().equals("so")) {
				//excelSoModule.addSoNoToEco();
				try {
					OkHttpClient client = new OkHttpClient();

					Request request = new Request.Builder()
							.url(qopsURL + "/api/so/addSoNoToEco")
							.get().addHeader("cache-control", "no-cache").build();

					Response response = client.newCall(request).execute();
				} catch (IOException e) {
					log.error("saveEcoToRfqDetailAndCreatEco error", e);
				}
			}
			
		}

		log.info("End of execute excel import.");
	}

	@SuppressWarnings("rawtypes")
	private void doExcelImport(ExcelData excelData) {
		try {
			// 1. 查找文件是否存在
			String filePath = excelData.getExcelLocation() + File.separator + excelData.getExcelName();
			if (!new File(filePath).exists()) {
				log.error("Didn't find this Excel File: {} - Skipped!", filePath);
				return;
			}

			log.info("Begin to read Excel File: {}", filePath);
			// 2. 开始读入excel文件
			ReadExcelUtils readUtil = new ReadExcelUtils(filePath);
			String[] titles = readUtil.readExcelTitle();

			// 3. 查询数据库中是否存在此数据表，如果没有，则标记创建
			String[] pkCols = excelData.getPkCols();
			String tableName = "excel_" + excelData.getNickname().replace('.', '_').replace(' ', '_');
			if (!excelDataDAO.checkTableExist(tableName)) {
				excelDataDAO.createExcelTable(tableName, titles, pkCols);
			}

			// 4. 查询出当前数据表中的主键数据
			List pkList = Arrays.asList(pkCols);
			Integer[] pkIndex = new Integer[pkCols.length];
			for (int i = 0; i < pkCols.length; i++) {
				String pkTitle = pkCols[i];
				for (int j = 0; j < titles.length; j++) {
					if (pkTitle.equals(titles[j])) {
						pkIndex[i] = j;
						break;
					}
				}
			}
			List<Map<String, Object>> pks = excelDataDAO.getPkValuesOfExcelTable(tableName, pkCols);

			// 5. 读入数据并导入数据库
			List<Map<Integer, Object>> records = readUtil.readExcelContent();
			int count = 0;
			List<String> sqls = new ArrayList<>();
			for (Map<Integer, Object> record : records) {
				Map<String, Object> pk = new HashMap<>();
				for (int i = 0; i < pkCols.length; i++) {
					pk.put(pkCols[i], record.get(pkIndex[i]));
				}

				// 通过主键字段判断数据是否已存在，如果不存在就做导入，如果存在就进行更新操作
				if (!pks.contains(pk)) {
					sqls.add(excelDataDAO.insertDataToExcelTableSql(tableName, titles, record));
				} else {
					sqls.add(excelDataDAO.updateDataToExcelTableSql(tableName, titles, record, pkList));
				}
				count ++;
				if (count % 1000 == 0) {
					excelDataDAO.batchUpdateSql(sqls);
					log.info("import record count: {}", count);
					sqls = new ArrayList<>();
				}
			}
			if (sqls.size() > 0) {
				excelDataDAO.batchUpdateSql(sqls);
			}
			log.info("Finished to read Excel File: {}", filePath);
		} catch (Exception ex) {
			log.error("import excel", ex);
		}
	}
	
	private void clearDuplicateRecordsInExcelEco() {
		List<Map<String, Object>> pks = excelDataDAO.getDuplicatePKsInExcelEco();
		if (pks != null) {
			for (Map<String, Object> pk : pks) {
				Map<String, Object> record = excelDataDAO.getRecordInExcelTable("eco", pk);
				log.info("record={}",record);
				
				excelDataDAO.deleteRecordInExcelTable("eco", pk);
				
				excelDataDAO.insertREcordIntoExcelTable("eco", record);
			}
		}
	}
}