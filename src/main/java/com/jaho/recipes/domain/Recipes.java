package com.jaho.recipes.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Recipes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recipe_id;
	
	@Column(unique=true)
	private String title;
	
	private Integer portionsize;
	
	private Integer minutes;
	
	private String recipe;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="recipe_id")
	private List<Ingredients> ingredients;

	public Recipes() {
		super();
	}

	public Recipes(String title, Integer portionsize, Integer minutes, String recipe) {
		super();
		this.title = title;
		this.portionsize = portionsize;
		this.minutes = minutes;
		this.recipe = recipe;
	}

	public Recipes(Long recipe_id, String title, Integer portionsize, Integer minutes, String recipe) {
		super();
		this.recipe_id = recipe_id;
		this.title = title;
		this.portionsize = portionsize;
		this.minutes = minutes;
		this.recipe = recipe;
	}
	
	public Recipes(long recipe_id, String title, int portionsize, int minutes, String recipe) {
		super();
		this.recipe_id = Long.valueOf(recipe_id);
		this.title = title;
		this.portionsize = Integer.valueOf(portionsize);
		this.minutes = Integer.valueOf(minutes);
		this.recipe = recipe;
	}

	public Long getRecipe_id() {
		return recipe_id;
	}

	public void setRecipe_id(Long recipe_id) {
		this.recipe_id = recipe_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPortionsize() {
		return portionsize;
	}

	public void setPortionsize(Integer portionsize) {
		this.portionsize = portionsize;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
	
	

	public List<Ingredients> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Recipes [recipe_id=" + recipe_id + ", title=" + title + ", portionsize=" + portionsize + ", minutes="
				+ minutes + ", recipe=" + recipe + "]";
	}
	
	

}
