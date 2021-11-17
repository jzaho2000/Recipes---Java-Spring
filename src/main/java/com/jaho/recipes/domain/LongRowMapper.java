package com.jaho.recipes.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LongRowMapper implements RowMapper<Long>{
	
	public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Long value = rs.getLong("recipe_id");
		
		return value;
		
	}

}
