package com.jaho.recipes.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipesRepositoryTest {

	
	@Autowired RecipesRepository repository;

	
	
	@Test
	void findAll() {
		//fail("Not yet implemented");
		
		List<Recipes> recipes = (List<Recipes>) repository.findAll();
		
		assertThat(recipes).isNotNull();
		assertThat(recipes).isNotEmpty();
		
	}
	
	
	@Test
	void save() {
		//fail("Not yet implemented");
		
		int before_size = ((List<Recipes>) repository.findAll()).size();
		
		
		Recipes recipe = new Recipes("AAAAAAAAAAAAAAAAAAAA", 5, 45, "dds dsad a\n dsad dasd dsa dsa ");
		Recipes saved_recipe = repository.save(recipe);
		
		int after_size = ((List<Recipes>) repository.findAll()).size();
		
		
		// Check if data were added
		assertThat(before_size).isEqualTo(after_size-1);
		assertThat(saved_recipe).isNotNull();
		assertThat(saved_recipe.getRecipe_id()).isNotNull();
		assertThat(saved_recipe.getRecipe_id()).isGreaterThan(0);
		
		
		// check if values were added correctly
		assertThat(saved_recipe.getTitle()).isEqualTo(recipe.getTitle());
		assertThat(saved_recipe.getPortionsize()).isEqualTo(recipe.getPortionsize());
		assertThat(saved_recipe.getMinutes()).isEqualTo(recipe.getMinutes());
		assertThat(saved_recipe.getRecipe()).isEqualTo(recipe.getRecipe());
		
		// check if we can add same recipe twice (answer should be no)
		before_size = ((List<Recipes>) repository.findAll()).size();
		recipe.setRecipe("fdsfds fdsf fds fsd fds");
		Recipes saved_recipe2 = repository.save(recipe);
		after_size = ((List<Recipes>) repository.findAll()).size();
		
		//assertThat(before_size).isEqualTo(after_size);
		//assertThat(saved_recipe2).isNull();
		//assertThat(saved_recipe2.getRecipe_id()).isNull();
		//assertThat(saved_recipe2.getRecipe_id()).isNegative();
		
		
		// delete the added data
		repository.deleteById(saved_recipe.getRecipe_id());
		
	}

}


