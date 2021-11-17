package com.jaho.recipes.domain;
import java.util.List;

import org.springframework.data.repository.CrudRepository;



public interface RecipesRepository extends CrudRepository<Recipes,Long> {

	List<Recipes> findByTitle(String title);
	
	List<Recipes> findByTitleContaining(String title);
}