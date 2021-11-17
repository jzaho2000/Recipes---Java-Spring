package com.jaho.recipes.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



public class ClosetRowMapper implements RowMapper<Closet> {
	
	public Closet mapRow(ResultSet rs, int rowNum) throws SQLException {
		Closet closet = new Closet();
		
		closet.setCloset_id(rs.getLong("closet_id"));
		closet.setUser_id(rs.getLong("user_id"));
		closet.setMaterial_id(rs.getLong("material_id"));

		return closet;
		
	}
	
	


}
