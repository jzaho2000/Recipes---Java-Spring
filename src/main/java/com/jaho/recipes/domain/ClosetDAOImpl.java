package com.jaho.recipes.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jaho.recipes.RecipesApplication;

import org.springframework.jdbc.core.RowMapper;

@Repository
public class ClosetDAOImpl implements ClosetDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(RecipesApplication.class);
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	public String save(long user_id, long material) {
		
		if (user_id<=0 || material<=0)
			return "Not valid id.";
				
		try {
			
			
			
			String sql = "select user_id, material_id from closet where user_id=(?) and material_id=(?)";
			Object[] parameters = new Object[] { user_id, material };
			RowMapper<Closet> mapper = new ClosetRowMapper();
			List<Closet> closet = jdbcTemplate.query(sql, mapper, parameters);
		
			if ( closet != null && closet.size()>0 )
				return "Closet already contain product!";
			
			sql = "INSERT INTO closet(user_id, material_id) VALUES ((?),(?))";
			parameters = new Object[] { user_id, material };
			jdbcTemplate.update(sql, parameters);
			
			
			/*
			String sql = "INSERT INTO closet(user_id, material_id) " +
			      "(SELECT id AS user_id, (?) AS material_id FROM user " + 
				  "WHERE user.id=(?) AND user.id NOT IN (SELECT user_id AS id FROM closet WHERE material_id=(?)))";
			Object[] parameters = new Object[] { material, user_id, material };
			
			jdbcTemplate.update(sql, parameters);
			*/
			
			
			
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return "";
	}
	
	@Override
	public String save(String username, long material) {
		
		if (username == null || material<=0)
			return "Not valid ID.";
		
		try {
			
			
			
			String sql = "SELECT A.user_id, A.material_id FROM closet AS A, user AS B " +
			             "WHERE A.user_id=B.id AND B.username=(?) AND A.material_id=(?)";
			Object[] parameters = new Object[] { username, material };
			RowMapper<Closet> mapper = new ClosetRowMapper();
			List<Closet> closet = jdbcTemplate.query(sql, mapper, parameters);
			
		
			if ( closet != null && closet.size()>0 )
				return "Closet already containt product!";
			
			sql = "INSERT INTO closet(user_id, material_id) SELECT id AS user_id, (?) AS material_id FROM user WHERE username=(?)";
			parameters = new Object[] { material, username };
			jdbcTemplate.update(sql, parameters);
			
			/*
			String sql = "INSERT INTO closet(user_id, material_id) VALUES " + 
			      "(SELECT id AS user_id, (?) AS material_id FROM user AS B " + 
				  "WHERE B.username=(?) AND B.id NOT IN (SELECT user_id as id FROM closet WHERE material_id=(?)))";
			Object parameters = new Object[] { Long.valueOf(material), username, Long.valueOf(material) };
		    
			jdbcTemplate.update(sql, parameters);
			*/
			
			
		} catch (Exception e) {
			return e.getMessage();
		}
		
		
		return "";
	}
	
	@Override
	public String delete(long user_id, long material) {
		if (user_id<=0 || material<=0)
			return "Not valid ID.";
				
		try {

			
			String sql = "DELETE FROM closet WHERE user_id=(?) AND material_id=(?)";
			
			Object[] parameters = new Object[] { user_id, material };
			jdbcTemplate.update(sql, parameters);
			
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return "";
	}
	
	@Override
	public String delete(String username, long material) {
		if (username == null || material<=0)
			return "Not valid ID.";
		
		try {

			
			String sql = "DELETE FROM closet WHERE material_id=(?) AND user_id IN (SELECT id AS user_id FROM user WHERE username=(?))";
			
			Object[] parameters = new Object[] { material, username };
			jdbcTemplate.update(sql, parameters);
			
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return "";
		
	}
	
	
	
	@Override
	public String save(long id, long user, long material) {
		
		if (id<=0 || user<=0 || material<=0)
			return "Not valid ID.";
		
		try {
			
			String sql = "CREATE TABLE IF NOT EXISTS closet (" +
					"closet_id BIGINT NOT NULL AUTO_INCREMENT," +
					"user_id BIGINT NOT NULL," +
					"material_id BIGINT NOT NULL," +
					"PRIMARY KEY (closet_id)," +
					"FOREIGN KEY (user_id) REFERENCES user(id)," +
					"FOREIGN KEY (material_id) REFERENCES materials(material_id)" +
				    ")";
			jdbcTemplate.update(sql);
			
			
			sql = "INSERT INTO closet(closet_id, user_id, material_id) VALUES ((?),(?),(?))";
			Object[] parameters = new Object[] { Long.valueOf(id), Long.valueOf(user), Long.valueOf(material) };
			jdbcTemplate.update(sql, parameters);
			
			
			
			
		} catch (Exception e) {
			return e.getMessage();
		}
		
		
		return "";
	}
	
	
	
	
	
	
	@Override
	public List<Materials> getClosetContent(long user_id) {
		
		List<Materials> list = new ArrayList<Materials>();
		
		if (user_id<=0)
			return list;
		
		try {
			
			// SELECT A.material_id, A.name from materials AS A, closet AS B WHERE A.material_id=B.material_id AND B.user_id=1
			String sql = "SELECT A.material_id, A.name FROM materials AS A, closet AS B WHERE A.material_id=B.material_id AND "+
			             "B.user_id=(?) ORDER BY A.name";
			Object[] parameters = new Object[] { user_id};
			RowMapper<Materials> mapper = new MaterialsRowMapper();
			list = jdbcTemplate.query(sql, mapper, parameters);
			
		} catch (Exception e) {
			return new ArrayList<Materials>();
		}
		
		
		return list;
	}
	
	@Override
	public List<Materials> getClosetContent(String username) {
		List<Materials> list = new ArrayList<Materials>();
		
		if (username == null)
			return list;
		
		try {
			
			
			//SELECT A.material_id, A.name from materials AS A, closet AS B, user AS C 
			//WHERE A.material_id=B.material_id AND B.user_id=C.id AND C.username='user';
			String sql = "SELECT A.material_id, A.name FROM materials AS A, closet AS B, user AS C " + 
			             "WHERE A.material_id=B.material_id AND B.user_id=C.id AND C.username=(?) ORDER BY A.name";
			Object[] parameters = new Object[] { username};
			RowMapper<Materials> mapper = new MaterialsRowMapper();
			list = jdbcTemplate.query(sql, mapper, parameters);
			
		} catch (Exception e) {
			return new ArrayList<Materials>();
		}
		
		
		return list;
	}
	
	
	
	
	@Override
	public List<Long> getSuggestedRecipes(long user_id) {
		List<Long> list = new ArrayList<Long>();
		
		if (user_id<=0)
			return list;
		
		try {
			
			
			
			String sql = "SELECT A.recipe_id FROM "
					+ "(SELECT C.recipe_id, count(*) AS lkm FROM recipes AS C, ingredients AS D "
					+      "WHERE C.recipe_id=D.recipe_id GROUP BY C.recipe_id) AS A, "
					+ "(SELECT G.recipe_id, count(*) AS lkm FROM closet AS F, ingredients AS G "
					+      "WHERE F.user_id=(?) AND F.material_id=G.material_id GROUP BY G.recipe_id) AS B "
					+ "WHERE A.lkm = B.lkm;";
			Object[] parameters = new Object[] { user_id };
			RowMapper<Long> mapper = new LongRowMapper();
			list = jdbcTemplate.query(sql, mapper, parameters);
			
		} catch (Exception e) {
			return new ArrayList<Long>();
		}
		
		
		return list;
	}
	
	@Override
	public List<Long> getSuggestedRecipes(String username) {
		List<Long> list = new ArrayList<Long>();
		
		if (username==null)
			return list;
		
		try {
			
			
			String sql = "SELECT A.recipe_id FROM "
					+ "(SELECT C.recipe_id, count(*) AS lkm FROM recipes AS C, ingredients AS D "
					+      "WHERE C.recipe_id=D.recipe_id GROUP BY C.recipe_id) AS A, "
					+ "(SELECT G.recipe_id, count(*) AS lkm FROM user AS E, closet AS F, ingredients AS G "
					+      "WHERE E.username=(?) AND E.id=F.user_id AND F.material_id=G.material_id GROUP BY G.recipe_id) AS B "
					+ "WHERE A.lkm = B.lkm;";
			Object[] parameters = new Object[] { username };
			RowMapper<Long> mapper = new LongRowMapper();
			list = jdbcTemplate.query(sql, mapper, parameters);
			
		} catch (Exception e) {
			return new ArrayList<Long>();
		}
		
		
		return list;
	}
	

}
