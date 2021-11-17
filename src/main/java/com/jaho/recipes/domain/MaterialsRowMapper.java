package com.jaho.recipes.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MaterialsRowMapper implements RowMapper<Materials>{
		
	public Materials mapRow(ResultSet rs, int rowNum) throws SQLException {
		Materials material = new Materials();
		
		material.setMaterial_id(rs.getLong("material_id"));
		material.setName(rs.getString("name"));

		return material;
		
	}

}
