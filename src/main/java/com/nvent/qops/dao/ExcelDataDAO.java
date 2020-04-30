package com.nvent.qops.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nvent.qops.entity.ExcelData;

@Service
@SuppressWarnings("rawtypes")
public class ExcelDataDAO {
	public static final Logger log = LoggerFactory.getLogger(ExcelDataDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private BeanPropertyRowMapper<ExcelData> excelDataRowMapper = new BeanPropertyRowMapper<ExcelData>(ExcelData.class);

	public List<ExcelData> getAllExcelData() {
		return jdbcTemplate.query("select * from excel_data;", excelDataRowMapper);
	}
	
	public ExcelData getExcelDataById(int id) {
		List<ExcelData> records = jdbcTemplate.query("select * from excel_data where id=?;", excelDataRowMapper, id);
		if (records.size() > 0) {
			return records.get(0);
		}
		return null;
	}

	public ExcelData getExcelDataByNickname(String nickname) {
		List<ExcelData> records = jdbcTemplate.query("select * from excel_data where nickname=?;", excelDataRowMapper, nickname);
		if (records.size() > 0) {
			return records.get(0);
		}
		return null;
	}

	public synchronized boolean createExcelData(ExcelData data) {
		Map ret = jdbcTemplate.queryForMap("select max(id) as max_id from excel_data;");
		int maxId = 0;
		if (ret.containsKey("max_id")) {
			maxId = ((Number)ret.get("max_id")).intValue();
		}
		maxId ++;
		jdbcTemplate.update("insert into excel_data(id,excel_location,excel_name,sheet_name,nickname,pk_columns) values(?,?,?,?,?,?);",
				maxId, data.getExcelLocation(), data.getExcelName(), data.getSheetName(), data.getNickname(), data.getPkColumns());
		return true;
	}
	
	public boolean updateExcelData(ExcelData data) {
		jdbcTemplate.update("update excel_data set excel_location=?,excel_name=?,sheet_name=?,nickname=?,pk_columns=? where id=?;",
				data.getExcelLocation(), data.getExcelName(), data.getSheetName(), data.getNickname(), data.getPkColumns(), data.getId());
		return true;
	}
	
	public boolean deleteExcelData(int id) {
		jdbcTemplate.update("delete from excel_data where id=?;", id);
		return true;
	}
	
	public boolean checkTableExist(String tableName) {
		Map result = jdbcTemplate.queryForMap("select count(*) as table_number from information_schema.TABLES WHERE TABLE_NAME=?;", tableName);
		if (result != null && result.containsKey("table_number")) {
			int num = ((Number)result.get("table_number")).intValue();
			if (num > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean createExcelTable(String tableName, String[] titles, String[] pkCols) {
		String sql = null;
		try {
			log.info("Create excel table: {}", tableName);
			
			StringBuilder sb = new StringBuilder("CREATE TABLE `").append(tableName).append("` (");
			
			for (String title : titles) {
				title = title.replace(' ', '_');
				sb.append('`').append(title).append("` varchar(255) NOT NULL,");
			}
			sb.append("PRIMARY KEY (");
			for (String pkCol : pkCols) {
				pkCol = pkCol.replace(' ', '_');
				sb.append('`').append(pkCol).append("`,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			
			sql = sb.toString();
			jdbcTemplate.update(sql);
			return true;
		} catch(Exception ex) {
			log.error("Error of execute sql: {}", sql);
			log.error("Create excel table error", ex);
		}
		
		return false;
	}
	
	public List<Map<String, Object>> getPkValuesOfExcelTable(String tableName, String[] pkCols) {
		StringBuilder sb = new StringBuilder("SELECT ");
		for (String col : pkCols) {
			col = col.replace(' ', '_');
			sb.append('`').append(col).append("`,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" FROM ").append(tableName).append(';');
		String sql = sb.toString();
		List<Map<String, Object>> records = jdbcTemplate.queryForList(sql);
		return records;
	}
	
	public String insertDataToExcelTableSql(String tableName, String[] titles, Map<Integer,Object> record) {
		String sql = null;
		try {
			StringBuilder sb = new StringBuilder("INSERT INTO ");
			sb.append(tableName).append('(');
			
			for (String title : titles) {
				title = title.replace(' ', '_');
				sb.append('`').append(title).append("`,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") VALUES(");
			
			for (int i = 0; i < titles.length; i ++) {
				String value = record.get(i).toString();
				value = value.replaceAll("'", "\\\\'");
				sb.append('\'').append(value).append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(");");
			
			sql = sb.toString();
			//jdbcTemplate.update(sql);
			return sql;
		} catch (Exception ex) {
			log.error("Error of execute sql: {}", sql);
			log.error("Error of insert data", ex);
		}
		return null;
	}

	public String updateDataToExcelTableSql(String tableName, List<String> titles, List<String> updateCols, Map<Integer,Object> record, List pks) {
		String sql = null;
		try {
			StringBuilder sb = new StringBuilder("UPDATE ");
			sb.append(tableName).append(" SET ");
			
			int size = updateCols.size();
			for (int i = 0; i < size; i ++) {
				String title = updateCols.get(i);
				int index = titles.indexOf(title);

				title = title.replace(' ', '_');
				sb.append('`').append(title).append("`=");
					
				String value = record.get(index).toString();
				value = value.replaceAll("'", "\\\\'");
				sb.append('\'').append(value).append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" WHERE ");
			
			size = titles.size();
			for (int i = 0; i < size; i ++) {
				String title = titles.get(i);
				if (pks.contains(title)) {
					title = title.replace(' ', '_');
					sb.append('`').append(title).append("`=");
					
					String value = record.get(i).toString();
					value = value.replaceAll("'", "\\\\'");
					sb.append('\'').append(value).append("' AND ");
				}
			}
			sb.delete(sb.length() - 5, sb.length());
			sb.append(";");
			
			sql = sb.toString();
			//jdbcTemplate.update(sql);
			return sql;
		} catch (Exception ex) {
			log.error("Error of execute sql: {}", sql);
			log.error("Error of update data", ex);
		}
		return sql;
	}
	
	public boolean batchUpdateSql(List<String> sql) {
		try {
			jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
			return true;
		} catch (Exception ex) {
			log.error("Error of execute sql: {}", sql);
			log.error("Error of update data", ex);
		}
		return false;
	}

	public List<Map<String, Object>> getDuplicatePKsInExcelEco() {
		List<Map<String, Object>> records = jdbcTemplate.queryForList("SELECT Item_Number,Order_Number FROM excel_eco group BY Item_Number,Order_Number having COUNT(*) > 1;");
		return records;
	}

	public Map<String, Object> getRecordInExcelTable(String tableName, Map<String, Object> pk) {
		StringBuilder sql = new StringBuilder("select * from excel_");
		sql.append(tableName).append(" where ");
		for(String key : pk.keySet()) {
			sql.append('`').append(key).append("`='").append(pk.get(key)).append("' and ");
		}
		sql.delete(sql.length() - 5, sql.length());
		sql.append(';');
		List<Map<String, Object>> records = jdbcTemplate.queryForList(sql.toString());
		if (records.size() > 0) {
			return records.get(0);
		}
		return null;
	}

	public void deleteRecordInExcelTable(String tableName, Map<String, Object> pk) {
		StringBuilder sql = new StringBuilder("delete from excel_");
		sql.append(tableName).append(" where ");
		for(String key : pk.keySet()) {
			sql.append('`').append(key).append("`='").append(pk.get(key)).append("' and ");
		}
		sql.delete(sql.length() - 5, sql.length());
		sql.append(';');
		jdbcTemplate.update(sql.toString());
	}

	public void insertREcordIntoExcelTable(String tableName, Map<String, Object> record) {
		StringBuilder sb = new StringBuilder("INSERT INTO excel_");
		sb.append(tableName).append('(');
		
		for (String title : record.keySet()) {
			title = title.replace(' ', '_');
			sb.append('`').append(title).append("`,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") VALUES(");
		
		for (String title : record.keySet()) {
			Object v = record.get(title);
			String value = (v == null) ? "" : v.toString();
			value = value.replaceAll("'", "\\\\'");
			sb.append('\'').append(value).append("',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");");
		jdbcTemplate.update(sb.toString());
	}

	public String updateDataToExcelTableSql(String tableName, String[] titles, Map<Integer,Object> record, List pks) {
		String sql = null;
		try {
			StringBuilder sb = new StringBuilder("UPDATE ");
			sb.append(tableName).append(" SET ");
			
			for (int i = 0; i < titles.length; i ++) {
				String title = titles[i];
				if (!pks.contains(title)) {
					title = title.replace(' ', '_');
					sb.append('`').append(title).append("`=");
					
					String value = record.get(i).toString();
					value = value.replaceAll("'", "\\\\'");
					sb.append('\'').append(value).append("',");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" WHERE ");
			
			for (int i = 0; i < titles.length; i ++) {
				String title = titles[i];
				if (pks.contains(title)) {
					title = title.replace(' ', '_');
					sb.append('`').append(title).append("`=");
					
					String value = record.get(i).toString();
					value = value.replaceAll("'", "\\\\'");
					sb.append('\'').append(value).append("' AND ");
				}
			}
			sb.delete(sb.length() - 5, sb.length());
			sb.append(";");
			
			sql = sb.toString();
			//jdbcTemplate.update(sql);
			return sql;
		} catch (Exception ex) {
			log.error("Error of execute sql: {}", sql);
			log.error("Error of update data", ex);
		}
		return sql;
	}

	public void dropExcelTable(String tableName) {
		jdbcTemplate.update("DROP TABLE IF EXISTS `" + tableName + "`;");
	}
}