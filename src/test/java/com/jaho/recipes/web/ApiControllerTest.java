package com.jaho.recipes.web;


/*
 *  Tämän paketin testit toimivat jos kytketään turva-asetukset pois päältä.
 * 
 * 
 * 
 * 
 */

import static org.assertj.core.api.Assertions.assertThat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.containsString;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.jaho.recipes.domain.Recipes;
import com.jaho.recipes.domain.RecipesRepository;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RecipesRepository recipesRepository;
	
	private final String JSON_TYPE = "application/json;charset=utf-8";
	

	
	@Test
	void getRecipeTest() {
		

		   try {
			   
			String jsonData = 
			           "{\"recipes\":[{\"ingredients\":[{\"amount\":\"2\",\"measure\":\"dl\",\"material\":\"Maito\"},"
					   + "{\"amount\":\"1\",\"measure\":\"kpl\",\"material\":\"Banaani\"},"
					   + "{\"amount\":\"0.5\",\"measure\":\"dl\",\"material\":\"Vaniljajäätelö\"}],"
					   + "\"recipe_id\":1,\"minutes\":15,\"title\":\"Banaanipirtelö\",\"portionsize\":1},"
					   + "{\"ingredients\":[{\"amount\":\"2\",\"measure\":\"dl\",\"material\":\"Maito\"},"
					   + "{\"amount\":\"500\",\"measure\":\"g\",\"material\":\"Mansikka\"},"
					   + "{\"amount\":\"0.5\",\"measure\":\"dl\",\"material\":\"Vaniljajäätelö\"}],"
					   + "\"recipe_id\":2,\"minutes\":14,\"title\":\"Mansikkapirtelö\",\"portionsize\":1},"
					   + "{\"ingredients\":[{\"amount\":\"3\",\"measure\":\"kpl\",\"material\":\"Muna\"},"
					   + "{\"amount\":\"6\",\"measure\":\"dl\",\"material\":\"Maito\"},"
					   + "{\"amount\":\"3\",\"measure\":\"dl\",\"material\":\"Vehnäjauho\"},"
					   + "{\"amount\":\"1\",\"measure\":\"rkl\",\"material\":\"Sokeri\"},"
					   + "{\"amount\":\"1\",\"measure\":\"tl\",\"material\":\"Suola\"}],"
					   + "\"recipe_id\":3,\"minutes\":75,\"title\":\"Lettutaikina\",\"portionsize\":2},"
					   + "{\"ingredients\":[{\"amount\":\"2\",\"measure\":\"kpl\",\"material\":\"Omena\"},"
					   + "{\"amount\":\"2\",\"measure\":\"kpl\",\"material\":\"Päärynä\"},"
					   + "{\"amount\":\"1\",\"measure\":\"tlk\",\"material\":\"Persikka\"},"
					   + "{\"amount\":\"100\",\"measure\":\"g\",\"material\":\"Viinirypäle\"},"
					   + "{\"amount\":\"2\",\"measure\":\"kpl\",\"material\":\"Kiivi\"},"
					   + "{\"amount\":\"2\",\"measure\":\"kpl\",\"material\":\"Appelsiini\"},"
					   + "{\"amount\":\"2\",\"measure\":\"kpl\",\"material\":\"Banaani\"}],"
					   + "\"recipe_id\":4,\"minutes\":15,\"title\":\"Hedelmäsalaatti\",\"portionsize\":8},"
					   + "{\"ingredients\":[{\"amount\":\"250\",\"measure\":\"g\",\"material\":\"Voita\"},"
					   + "{\"amount\":\"2\",\"measure\":\"dl\",\"material\":\"Sokeri\"},"
					   + "{\"amount\":\"3\",\"measure\":\"kpl\",\"material\":\"Muna\"},"
					   + "{\"amount\":\"3.5\",\"measure\":\"dl\",\"material\":\"Vehnäjauho\"},"
					   + "{\"amount\":\"2\",\"measure\":\"tl\",\"material\":\"Vaniliinisokeria\"},"
					   + "{\"amount\":\"2\",\"measure\":\"tl\",\"material\":\"Leivinjauhe\"},"
					   + "{\"amount\":\"3\",\"measure\":\"rkl\",\"material\":\"Kaakaojauhetta\"},"
					   + "{\"amount\":\"3\",\"measure\":\"rkl\",\"material\":\"Kerma\"}],"
					   + "\"recipe_id\":5,\"minutes\":75,\"title\":\"Tiikerikakku\",\"portionsize\":8}]}";
			   
			this.mockMvc.perform(get("http://localhost:8080/api/recipes"))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect( content().json(jsonData) );
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		   
	}
	
	
	
	@Test
	void addRecipeTest() throws Exception {
		

			   
		List<Recipes> r = this.recipesRepository.findByTitle("test recipe");
		assertThat(r).isNotNull();
		assertThat(r.size()).isEqualTo(0);
		
		String recipe = "{\"title\":\"test recipe\", \"minutes\":18, \"portionsize\":10, \"recipe\":\"ohjeita ruuan tekoon\", " + 
		"\"ingredients\":[{\"amount\":1, \"measure\": 2, \"material\": 3}]}";
		this.mockMvc.perform(post("http://localhost:8080/api/recipes").content(recipe).contentType(this.JSON_TYPE))
		   .andDo(print())
		   .andExpect(status().isOk())
		   .andExpect(content().json("{\"success\": 1}"));
		
		r = this.recipesRepository.findByTitle("test recipe");
		assertThat(r).isNotNull();
		assertThat(r.size()).isEqualTo(1);

	
		   
	}
	
	
	
	
	
	@Test
	void deleteRecipeTest() throws Exception {
	
		List<Recipes> r = this.recipesRepository.findByTitle("test recipe");
		assertThat(r).isNotNull();
		assertThat(r.size()).isEqualTo(1);
		
		long id = r.get(0).getRecipe_id();
		
		this.mockMvc.perform(delete("http://localhost:8080/api/recipes").
				             contentType(this.JSON_TYPE).content("{\"id\":" + id +  "}"))
		   .andDo(print())
		   .andExpect(status().isOk())
		   .andExpect(content().json("{\"success\": 1}"));
		
		
		r = recipesRepository.findByTitle("test recipe");
		assertThat(r).isNotNull();
		assertThat(r.size()).isEqualTo(0);
		
	}	
		

}
