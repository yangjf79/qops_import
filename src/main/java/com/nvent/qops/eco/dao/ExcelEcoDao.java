package com.nvent.qops.eco.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nvent.qops.eco.entity.ExcelEco;

@Service
public class ExcelEcoDao {
	public static final Logger log = LoggerFactory.getLogger(ExcelEcoDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private BeanPropertyRowMapper<ExcelEco> excelEcoRowMapper = new BeanPropertyRowMapper<ExcelEco>(ExcelEco.class);

	// Check Fin Submit
	public boolean checkFinSubmitTrue(String sql) {
		List<ExcelEco> ecos = jdbcTemplate.query(sql, excelEcoRowMapper);
		if (ecos.size() > 0) {
			ExcelEco eco = ecos.get(0);
			return eco.isFinSubmit();
		}
		return false;
	}

}
