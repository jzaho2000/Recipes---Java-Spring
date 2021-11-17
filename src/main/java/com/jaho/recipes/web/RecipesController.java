package com.jaho.recipes.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jaho.recipes.RecipesApplication;
import com.jaho.recipes.domain.ClosetDAO;
import com.jaho.recipes.domain.Ingredients;
import com.jaho.recipes.domain.IngredientsDAO;
import com.jaho.recipes.domain.Materials;
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.Measurements;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.Recipes;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.User;
import com.jaho.recipes.domain.UserRepository;




@Controller
public class RecipesController {
	
	@Autowired MaterialsRepository materialsRepository;
	@Autowired MeasurementsRepository measurementsRepository;
	@Autowired RecipesRepository recipesRepository;
	@Autowired UserRepository userRepository;
	@Autowired IngredientsDAO ingredientsDAO;
	@Autowired ClosetDAO closetDAO;
	
	private static final Logger log = LoggerFactory.getLogger(RecipesApplication.class);
	
	private String errors = "";
	private String notices = "";
	private String form = "";
	
	@RequestMapping("/")
	public String root() {
		return "redirect:/recipes";
	}
	
	@RequestMapping(value="/recipes", method=RequestMethod.GET)
	public void recipes( @RequestParam(name="search", defaultValue="") String search, 
			             Model model ) {
		
		
		Iterable<Recipes> recipes = recipesRepository.findAll();
		
		if ( ! search.trim().equals("") ) {
			recipes = recipesRepository.findByTitleContaining( search );
		}
		
		
		model.addAttribute("recipes", recipes);
		
		errors = "";
		notices = "";
		
	}
	
	
	@RequestMapping("/suggestions")
	public void foodclosetRecipes(Model model) {
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<Long> recipe_ids = closetDAO.getSuggestedRecipes(username);
		Iterable<Recipes> recipes = recipesRepository.findAllById(recipe_ids);
		
		
		model.addAttribute("recipes", recipes);
		
		errors = "";
		notices = "";
		
	}
	
	 
	 
	 
	
		 
		 
		 
		
		@RequestMapping(value = "/show_recipe/{id}", method = RequestMethod.GET)
		public String showRecipe(@PathVariable("id") Long recipe_id, Model model) {
			 
			 if (recipe_id==null  || recipe_id.longValue()<=0) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../recipes";
			 }
			 
			 
			 Optional<Recipes> recipe = recipesRepository.findById(recipe_id);
			 if ( recipe == null || recipe.get() == null) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../recipes";
			 }
			 
			 
			 //model.addAttribute("errors", this.errors);
			 this.notices = "";
			 this.errors = "";
			 model.addAttribute("recipe", recipe.get() );
			 
			 return "show_recipe";
			 
		 }
		 
		
		
		
		


		@RequestMapping(value = "/edit_recipe/{id}", method = RequestMethod.GET)
		@PreAuthorize("hasAuthority('ADMIN')")
		public String editrecipe(@PathVariable("id") Long recipe_id, Model model) {
			 
			 if (recipe_id==null  || recipe_id.longValue()<=0) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../recipes";
			 }
			 
			 
			 Optional<Recipes> recipes = recipesRepository.findById(recipe_id);
			 if ( recipes == null || recipes.get()== null) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../recipes";
			 }
			 
			 String ingredients_table = "";
			 List<Ingredients> list = recipes.get().getIngredients();
			 if (list != null) {
				 for (int i=0; i<list.size(); i++) {
					 ingredients_table += "<td><input name=\"amount[" + i + "]\" value=\"" + list.get(i).getAmount() + "\" /></td>";
					 ingredients_table += "<td><input name=\"measure[" + i + "]\" value=\"" + list.get(i).getMeasurements().getMeasure_id() + "\" /></td>";
					 ingredients_table += "<td><input name=\"amount[" + i + "]\" value=\"" + list.get(i).getAmount() + "\" /></td>";
				 }
			 }
			
			 
			 model.addAttribute("forms", this.form);
			 model.addAttribute("notices", this.notices);
			 model.addAttribute("errors", this.errors);
			 model.addAttribute("recipes", recipes.get());
			 
			 
			 model.addAttribute("ingredients", recipes.get().getIngredients());
			 model.addAttribute("measurements", measurementsRepository.findAll() );
			 model.addAttribute("materials", materialsRepository.findAll());
			 
			 model.addAttribute("tabledata", ingredients_table);
			 
			 this.form = "";
			 this.errors = "";
			 this.notices = "";
			 
			 return "edit_recipe";
			 
		 }
		 
			 
			 
			 
			 
			 
			 
			 
			 
			@RequestMapping(value = "/edit_recipeheader", method = RequestMethod.POST)
			@PreAuthorize("hasAuthority('ADMIN')")
			public String editrecipeHeader(@RequestParam(name="recipe_id", defaultValue="0") Long recipe_id,
					                        @RequestParam(name="title", defaultValue="") String title,
					                        @RequestParam(name="portionsize", defaultValue="0") Integer portionsize,
					                        @RequestParam(name="minutes", defaultValue="0") Integer minutes) {
				if ( recipe_id.longValue()<=0 || title.trim().equals("") || portionsize.intValue()<=0 || minutes.intValue()<=0) {
					this.form = "1";
					this.errors = "Validation error(s)!";
					this.notices = "";
					
					if (recipe_id>0)
						return "redirect:edit_recipe/" + recipe_id;
					
					this.form = "";
					return "redirect:recepies";
				}
				
				try {
				
					Optional<Recipes> orecipe = recipesRepository.findById(recipe_id);
					
					if (orecipe == null || orecipe.get() == null) {
						this.form = "";
						this.errors = "ID not found!";
						this.notices = "";
						return "redirect:recepies";
					}
					
					Recipes recipe = orecipe.get();
					
					recipe.setTitle(title);
					recipe.setMinutes(minutes);
					recipe.setPortionsize(portionsize);
					
					recipesRepository.save(recipe);
					
					this.form = "1";
					this.errors = "";
					this.notices = "Header were saved succesfully!";
					
					return "redirect:edit_recipe/" + recipe_id.longValue();
				} catch (Exception e) {
					this.form = "1";
					this.errors = e.getMessage();
					this.notices = "";
					return "redirect:edit_recipe/" + recipe_id.longValue();
				}
			}
				
			 
			 
			
			
			@RequestMapping(value = "/edit_recipeinstruction", method = RequestMethod.POST)
			@PreAuthorize("hasAuthority('ADMIN')")
			public String editrecipeInstructions(@RequestParam(name="recipe_id", defaultValue="0") Long recipe_id,
					                        @RequestParam(name="instructions", defaultValue="") String instructions) {
				if ( recipe_id.longValue()<=0 || instructions.trim().equals("") ) {
					this.form = "3";
					this.errors = "Validation error(s)!";
					this.notices = "";
					
					if (recipe_id>0)
						return "redirect:edit_recipe/" + recipe_id;
					
					this.form = "";
					return "redirect:recepies";
				}
				
				try {
				
					Optional<Recipes> orecipe = recipesRepository.findById(recipe_id);
					
					if (orecipe == null || orecipe.get() == null) {
						this.form = "";
						this.errors = "ID not found!";
						this.notices = "";
						return "redirect:recepies";
					}
					
					Recipes recipe = orecipe.get();
					
					recipe.setRecipe(instructions);
					
					recipesRepository.save(recipe);
					
					this.form = "3";
					this.errors = "";
					this.notices = "Instruction were saved succesfully!";
					
					return "redirect:edit_recipe/" + recipe_id.longValue();
				} catch (Exception e) {
					this.form = "3";
					this.errors = e.getMessage();
					this.notices = "";
					return "redirect:edit_recipe/" + recipe_id.longValue();
				}
			}
			 
			 
			 
			 
			 
			 @RequestMapping(value = "/edit_recipeingredients", method = RequestMethod.POST)
			 @PreAuthorize("hasAuthority('ADMIN')")
			 public String editrecipentIngredients(@RequestBody String postBody) {
				 
				 if (postBody == null || postBody.trim().equals("") ) {
					 this.form = "";
					 this.errors = "Validation error(s)!";
					 this.notices = "";
					 return "redirect:/recipes";
				 }
				 
				 String[] params = postBody.split("&");
				 String[] help;
				 String str;
				 long recipe_id = -1;
				 double val;
				 
				 ArrayList<String> p = new ArrayList<String>();
				 ArrayList<String> v = new ArrayList<String>();
				 
				 
				
				 try {
			            
			            for (int i=0;i<params.length; i++) {
			            	
							 str = URLDecoder.decode(params[i], "UTF-8");
							 help = str.split("=");
							 if (help != null && help.length==2) {
								 p.add(help[0]);
								 v.add(help[1]);
								 
													 
								 
								 if ( help[0].equals("recipe_id") )
									 recipe_id = (long)Integer.parseInt(help[1]);
							 }
							 
			            	 
			            }
			            
			     } catch (Exception e) {
			        	this.form = "2";
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 if (recipe_id<0) {
							 this.form ="";
							 return "redirect:/recipes";
						 }
						 return "redirect:/edit_recipes/" + recipe_id;
			     }
				 
				 params = null;
				 
				 
				 if (recipe_id<0) {
					 this.form = "";
					 this.errors = "ID not found!!";
					 this.notices = "";
					 return "redirect:../recipes"; 
				 }
				 
				 
				 
				 Optional<Recipes> orecipe = recipesRepository.findById( Long.valueOf(recipe_id) );
				 
				 if (orecipe==null || orecipe.get() == null) {
					 this.form = "";
					 this.errors = "ID not found!!";
					 this.notices = "";
					 return "redirect:/recipes";
				 }
				 
				 Recipes recipe = orecipe.get();
				 orecipe = null;
				 
				 
				 boolean changes = false;
				 boolean err = false;
				 long id;
				 int index;
				 double amount;
				 long measurement;
				 long material;
				 Ingredients ing;
				 ArrayList<Integer> remove = new ArrayList<Integer>();
				 
				 List<Ingredients> ingredients = recipe.getIngredients();
				 
				 if (ingredients != null) {
					 
					 
					 try {
						 
						 for (int i=0; i<ingredients.size(); i++) {
							 
							 changes = false;
							 err =false;
							 
							 
							 ing = ingredients.get(i);
							 id = ing.getIngredient_id().longValue();
							 
							 
							 
							 index = p.indexOf("amount[" + id + "]");
							 if (index<0)
								 break;
							 amount = Double.parseDouble(v.get(index));
							 
							 index = p.indexOf("measure[" + id + "]");
							 if (index<0)
								 break;
							 measurement = Long.parseLong(v.get(index));
							 
							 index = p.indexOf("material[" + id + "]");
							 if (index<0)
								 break;
							 material = Long.parseLong(v.get(index));
							 
							 
							 // delete if amount <= 0
							 if (amount <= 0) {
								 
								 remove.add(Integer.valueOf(i));
								 
								 
						     // otherwise check changes
							 } else {
								 
								 if (amount != ing.getAmount().doubleValue() ) {
									 
									 changes = true;
								 }
								 
								 if (measurement != ing.getMeasurements().getMeasure_id() ) {
									 Optional<Measurements> m = measurementsRepository.findById(Long.valueOf(measurement));
									 
									 // check if measure exists, if not then mark it as error.
									 if (m != null && m.get() != null) {
										 
										 changes = true;
										 
									 } else {
										 err = true;
									 }
								 }
								 
								 if (material != ing.getMaterials().getMaterial_id() ) {
									 Optional<Materials> m = materialsRepository.findById(Long.valueOf(material));
									 
									// check if material exists, if not then mark it as error.
									 if (m != null && m.get() != null) {
										 
										 changes = true;
									 } else {
										 err = true;
									 }
								 }
								 
								 if (changes && !err) {
									 
									 ingredientsDAO.save(id,recipe_id, amount, measurement, material);
									 
								 }
								 
							 }
							 
							 
						 }
						 
						 
						 
						 // remove values. 
						 if (remove.size()>0) {
							 
							 for (int i=remove.size()-1; i>=0; i--) {
								 
								 index = remove.get(i).intValue();
								 ingredientsDAO.delete(ingredients.get(index).getIngredient_id());
								 
							 }
						 }
						 
						 
						 
					} catch (Exception e) {
						this.form = "2";
						this.errors = "Validation error(s)! " + e.getMessage();
						this.notices = "";
						
						return "redirect:/edit_recipe/" + recipe_id;
						 
					 }
					 
					 
				 }
				 
				 
				 try {
					 
				 
					 for (int i=1;i<=5; i++) {
						 
						 index = p.indexOf("addamount[" + i + "]");
						 if (index<0)
							 break;
						 amount = Double.parseDouble(v.get(index));
						 
						 
						 index = p.indexOf("addmeasure[" + i + "]");
						 if (index<0)
							 break;
						 measurement = Long.parseLong(v.get(index));
						 
						
						 index = p.indexOf("addmaterial[" + i + "]");
						 if (index<0)
							 break;
						 material = Long.parseLong(v.get(index));
						 
						 
						 if (amount>0 && measurement>0 && material>0) {
							 
							 
							 boolean check = true;
							 Optional<Measurements> ome = measurementsRepository.findById(measurement);
							 if (ome == null  || ome.get() == null) 
								check = false;
							 
							 
							 Optional<Materials> oma = materialsRepository.findById(material);
							 if (oma == null || oma.get() == null)
								 check = false;
							 
							 
							 if (check) {
								 
								 ingredientsDAO.save(recipe_id, amount, measurement, material);
								 
							 }
							 
							 
						 }
						 
					 }
				 } catch (Exception e) {
					 this.form = "2";
					this.errors = "Validation error(s)! " + e.getMessage();
					this.notices = "";
					
					return "redirect:/edit_recipe/" + recipe_id;
				 }
				 
				
				 this.form="2";
				 this.notices="Ingredients were added suffesfully!";
				 this.errors = "";
			 
				 return "redirect:/edit_recipe/" + recipe_id;
			 }
			 
			 
			 
			 
			 @RequestMapping(value = "/add_recipe", method = RequestMethod.GET)
			 @PreAuthorize("hasAuthority('ADMIN')")
			public String AddRecipe(Model model) {
					 
					 
					 model.addAttribute("notices", this.notices);
					 model.addAttribute("errors", this.errors);
					 model.addAttribute("measurements", measurementsRepository.findAll() );
					 model.addAttribute("materials", materialsRepository.findAll());
					 
					 
					 
					 
					 this.errors = "";
					 this.notices = "";
					 
					 return "add_recipe";
					 
				 }
			 
			 
			 
			 
			 
			 
			@RequestMapping(value = "/add_recipe", method = RequestMethod.POST)
			@PreAuthorize("hasAuthority('ADMIN')")
			public String SaveAddRecipe(@RequestBody String postBody) {
				 
				 if (postBody == null || postBody.trim().equals("") ) {
					 this.form = "";
					 this.errors = "Validation error(s)!";
					 this.notices = "";
					 return "redirect:/recipes";
				 }
				 
				 String[] params = postBody.split("&");
				 String[] help;
				 String str;
				 
				 double val;
				 
				 ArrayList<String> p = new ArrayList<String>();
				 ArrayList<String> v = new ArrayList<String>();
				 
				 
				
				 try {
			            
			            for (int i=0;i<params.length; i++) {
			            	
							 str = URLDecoder.decode(params[i], "UTF-8");
							 help = str.split("=");
							 if (help != null && help.length==2) {
								 p.add(help[0]);
								 v.add(help[1]);
								 
													 
								 
								 
							 }
							 
			            	 
			            }
			            
			     } catch (Exception e) {
			        	
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe";
			     }
				 
				 params = null;
				 
				 
				 int index;
				 int value;
				 Recipes recipe = new Recipes();
				 
				 
				 long recipe_id;
				 double amount;
				 long measurement;
				 long material;
				 
				 ArrayList<Double> l_amount = new ArrayList<Double>();
				 ArrayList<Long> l_measurement = new ArrayList<Long>();
				 ArrayList<Long> l_material   = new ArrayList<Long>();
				 
				 
				 try {
					 
					 
					 // lets try to create recipe
					 
					 index = p.indexOf("title");
					 if ( index<0 || v.get(index).trim().equals("") ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 
					 recipe.setTitle(v.get(index));
					 
					 
					 
					 index = p.indexOf("portionsize");
					 if ( index<0 ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 value = Integer.parseInt(v.get(index));
					 if ( value<=0 ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 recipe.setPortionsize(value);
					 
					 
					 index = p.indexOf("minutes");
					 if ( index<0 ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 value = Integer.parseInt(v.get(index));
					 if ( value<=0 ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 recipe.setMinutes(value);
					 
					 
					 
					 index = p.indexOf("instructions");
					 if ( index<0 || v.get(index).trim().equals("") ) {
						 this.errors = "Validation error(s)!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 recipe.setRecipe(v.get(index));
					 
					 
					 
					 
					 for (int i=1; i<=10; i++) {
						 
						 index = p.indexOf("addamount[" + i + "]");
						 if (index<0)
							 break;
						 amount = Double.parseDouble(v.get(index));
						 
						 
						 index = p.indexOf("addmeasure[" + i + "]");
						 if (index<0)
							 break;
						 measurement = Long.parseLong(v.get(index));
						 
						
						 index = p.indexOf("addmaterial[" + i + "]");
						 if (index<0)
							 break;
						 material = Long.parseLong(v.get(index));
						 
						 
						 if (amount>0 && measurement>0 && material>0) {
							 
							 
							 boolean check = true;
							 
							 Optional<Measurements> ome = measurementsRepository.findById(measurement);
							 if (ome == null  || ome.get() == null) 
								check = false;
							 
							 
							 Optional<Materials> oma = materialsRepository.findById(material);
							 if (oma == null || oma.get() == null)
								 check = false;
							 
							 
							 if (check) {
								 
								 l_amount.add(Double.valueOf(amount));
								 l_measurement.add(Long.valueOf(measurement));
								 l_material.add(Long.valueOf(material));
								 
							 }
							 
							 
						 }
						 
						 
						 
					 }
					 
					 
					 
					 
					 // when everything has been validated, then we will save the data.
					 Recipes r = recipesRepository.save(recipe);
					 
					 if (r == null || r.getRecipe_id()<=0) {
						 this.errors = "There were error when try to create ingredients!";
						 this.notices = "";
						 return "redirect:/add_recipe/";
					 }
					 
					 recipe_id = r.getRecipe_id();
					 for (int i=0;i<l_amount.size(); i++) {
						 ingredientsDAO.save(recipe_id, l_amount.get(i).doubleValue(), l_measurement.get(i).longValue(), l_material.get(i).longValue());
					 }
					 
					 
					 
				 
				 } catch (Exception e) {
					 this.errors = "Validation error(s)! " + e.getMessage();
					 this.notices = "";
					 return "redirect:/add_recipe/";
				 }
				 
				 this.errors = "";
				 this.notices = "recipe were saved succesfully!";
				 return "redirect:/add_recipe/";
				 
				 
			}
			
			
			
			
			@RequestMapping(value = "/delete_recipe/{id}", method = RequestMethod.GET)
			@PreAuthorize("hasAuthority('ADMIN')")
			public String deleterecipe(@PathVariable("id") Long recipe_id, Model model) {
				 
				 if (recipe_id==null  || recipe_id.longValue()<=0) {
					 this.errors = "Not valid id value!";
					 this.notices = "";
					 return "redirect:../recipes";
				 }
				 
				 try {
					 ingredientsDAO.deleteByRecipe(recipe_id);
					 recipesRepository.deleteById(recipe_id);
				 } catch (Exception e) {
					 this.errors = "There were error when deleting recipe! " + e.getMessage();
					 this.notices = "";
					 return "redirect:../recipes";
				 }
				 
				 this.errors = "";
				 this.notices = "recipe were deleted succesfully!";
				 return "redirect:../recipes";
			}
			 
			 
}
 