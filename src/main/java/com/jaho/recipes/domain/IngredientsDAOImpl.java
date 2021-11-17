package com.jaho.recipes.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientsDAOImpl implements IngredientsDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void save(long recipe_id, double amount, long measurement, long material) {
		String sql = "insert into ingredients(recipe_id, amount, measure_id, material_id) values(?,?,?,?)";
		Object[] parameters = new Object[] { recipe_id, amount, measurement, material};
		
		jdbcTemplate.update(sql, parameters);
		
	}
	
	@Override
	public void save(long ingredient_id, long recipe_id, double amount, long measurement, long material) {
		String sql = "update ingredients set recipe_id=(?), amount=(?), measure_id=(?), material_id=(?) where ingredient_id=(?)";
				
		Object[] parameters = new Object[] { recipe_id, amount, measurement, material, ingredient_id};
		
		jdbcTemplate.update(sql, parameters);
		
		
	}
	
	
	@Override
	public void createData(long ingredient_id, long recipe_id, long material_id, long measure_id, double amount) {
		
		try {
			
			String sql = "IF NOT EXISTS CREATE TABLE ingredients " 
					+ "	ingredient_id BIGINT NOT NULL AUTO_INCREMENT,"
					+ "	recipe_id BIGINT NOT NULL, "
					+ "	material_id BIGINT NOT NULL, "
					+ "	measure_id  BIGINT NOT NULL, "
					+ "	amount      DOUBLE NOT NULL, "
					+ "	PRIMARY KEY (ingredient_id), "
					+ "	FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id), "
					+ "	FOREIGN KEY (material_id) REFERENCES materials(material_id), "
					+ "	FOREIGN KEY (measure_id) REFERENCES measurements(measure_id) "
					+ ")";
			jdbcTemplate.update(sql);
			
			sql = "INSERT into ingredients(ingredient_id, recipe_id, material_id, measure_id, amount) VALUES " +
			             "((?),(?),(?),(?),(?))";
			Object[] parameters = new Object[] { ingredient_id, recipe_id, material_id, measure_id, amount};
			jdbcTemplate.update(sql, parameters);
		} catch (Exception e) {
			
		}
		
		
		
	}

	@Override
	public void delete(long ingredients_id) {
		String sql = "delete from ingredients where ingredient_id=?";
		Object[] parameters = new Object[] { Long.valueOf(ingredients_id) };
		
		jdbcTemplate.update(sql, parameters);
		
	}

	@Override
	public void deleteByRecipe(long recipe_id) {
		String sql = "delete from ingredients where recipe_id=?";
		Object[] parameters = new Object[] { Long.valueOf(recipe_id) };
		
		jdbcTemplate.update(sql, parameters);
		
	}

	

	
	
	
}
