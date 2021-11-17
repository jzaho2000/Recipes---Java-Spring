package com.jaho.recipes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Ingredients {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ingredient_id;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="recipe_id")
	private Recipes recipes;
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="material_id")
	private Materials materials;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="measure_id")
	private Measurements measurements;
	
	private Double amount;

	public Ingredients() {
		super();
	}

	public Ingredients(Long ingredient_id, Recipes recipes, Materials materials, Measurements measurements,
			Double amount) {
		super();
		this.ingredient_id = ingredient_id;
		this.recipes = recipes;
		this.materials = materials;
		this.measurements = measurements;
		this.amount = amount;
	}

	public Ingredients(Recipes recipes, Materials materials, Measurements measurements, Double amount) {
		super();
		this.recipes = recipes;
		this.materials = materials;
		this.measurements = measurements;
		this.amount = amount;
	}

	public Long getIngredient_id() {
		return ingredient_id;
	}

	public void setIngredient_id(Long ingredient_id) {
		this.ingredient_id = ingredient_id;
	}

	public Recipes getRecipes() {
		return recipes;
	}

	public void setRecipes(Recipes recipes) {
		this.recipes = recipes;
	}

	public Materials getMaterials() {
		return materials;
	}

	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	public Measurements getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Measurements measurements) {
		this.measurements = measurements;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	public String getAmountStr() {
		if ( isHoleNumber(this.amount) )
			return "" + (int)Math.floor(this.amount.doubleValue());
		
		return "" + this.amount.doubleValue();
	}
	
	
	private boolean isHoleNumber(Double value) {
		if (value == null)
			return false;
		
		double val = value.doubleValue();
		
		return (val == Math.floor(val));
	}
	
	
	
	
}
