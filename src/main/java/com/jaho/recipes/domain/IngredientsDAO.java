package com.jaho.recipes.domain;

public interface IngredientsDAO {
	
	public void save(long ingredient_id, long recipe_id, double amount, long measurement, long material);
	
	public void save(long recipe_id, double amount, long measurement, long material);
	
	public void createData(long ingredient_id, long recipe_id, long material_id, long measure_id, double amount);
	
	public void delete(long ingredients_id);
	
	public void deleteByRecipe(long recipe_id);

}
