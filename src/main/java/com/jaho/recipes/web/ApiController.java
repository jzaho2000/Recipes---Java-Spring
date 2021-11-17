package com.jaho.recipes.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaho.recipes.domain.ClosetDAO;
import com.jaho.recipes.domain.Ingredients;
import com.jaho.recipes.domain.IngredientsDAO;
import com.jaho.recipes.domain.Materials;
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.Measurements;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.Recipes;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.UserRepository;

@RestController
public class ApiController {

	private final AtomicLong counter = new AtomicLong();
	
	@Autowired MaterialsRepository materialsRepository;
	@Autowired MeasurementsRepository measurementsRepository;
	@Autowired RecipesRepository recipesRepository;
	@Autowired UserRepository userRepository;
	@Autowired IngredientsDAO ingredientsDAO;
	@Autowired ClosetDAO closetDAO;
	
	
	
	private Map<String, Object> recipesToMap(List<Recipes> recipes, boolean admin_level) {
		Map<String, Object> map = new HashMap<>();
		
		if (recipes == null) 
			return map;
		
		
		Recipes r;
		Ingredients in;
		
		ArrayList<Object> recipes_list = new ArrayList<Object>();
		for (int i=0; i<recipes.size(); i++) {
			r = recipes.get(i);
			
			Map<String, Object> recipe = new HashMap<>();
			recipe.put("recipe_id", r.getRecipe_id());
			recipe.put("title", r.getTitle());
			recipe.put("minutes", r.getMinutes());
			recipe.put("portionsize", r.getPortionsize());
			

			
			ArrayList<Object> ingredients = new ArrayList<Object>();
			for (int j=0; j<r.getIngredients().size(); j++) {
				in = r.getIngredients().get(j);
				
				Map<String, Object> ingredient = new HashMap<>();
				
				ingredient.put("amount", in.getAmountStr());
				ingredient.put("measure", in.getMeasurements().getShort_text() );
				ingredient.put("material", in.getMaterials().getName() );
				if (admin_level) {
					ingredient.put("ingredient_id", in.getIngredient_id() );
					ingredient.put("measure_id", in.getMeasurements().getMeasure_id());
					ingredient.put("material_id", in.getMaterials().getMaterial_id());
				}
				
				ingredients.add(ingredient);
				
				
			}	
			recipe.put("ingredients", ingredients);
			
			recipes_list.add(recipe);
		}
		
		map.put("recipes", recipes_list);
		
			
		
		
		
		return map;
	}
	
	
	
	
	
	@RequestMapping(value="/api/suggestions", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object>apiSuggestions() {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<Long> ids = closetDAO.getSuggestedRecipes(username);
		
		
		List<Recipes> suggestions = (List<Recipes>) recipesRepository.findAllById(ids);
		
		return recipesToMap(suggestions, false);
		

		
		
		
	}
	
	
	
	@RequestMapping(value="/api/recipes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> apiUserRecipies(@RequestParam(value="search", defaultValue="") String search) {
		
		List<Recipes> recipes = new ArrayList<Recipes>();
		boolean admin_level = false;
		
		if ( ! search.trim().equals("") ) {
			recipes = recipesRepository.findByTitleContaining(search);
		} else {
			
			recipes = (List<Recipes>) recipesRepository.findAll();
			
		}
		
		
		try {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ADMIN"))) {
				admin_level = true;
			}
				
			
			
		} catch (Exception e) {
			
		}
		
		return recipesToMap(recipes, admin_level);
		
		
	}
	
	
	
	
	
	@RequestMapping(value="/api/recipes", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String apiAddRecipies(@RequestBody Map<String, Object> payload) {

		
	
		Recipes recipe = new Recipes();
		String error_message = "{\"error\":1}";
		try {
			
			Set<String> keys = payload.keySet();
			Iterator<String> it = keys.iterator();
			String k;
			int r_id = 0;
			int i_id = 0;
			while (it.hasNext() == true) {
				k = it.next();
				
				if ( k.equals("title") ) {
					if (recipe.getTitle() != null)
						return error_message  + 1;
					
					recipe.setTitle(payload.get(k).toString());
					r_id++;
					
				} else if (k.equals("portionsize")) {
					if (recipe.getPortionsize() != null)
						return error_message + 2;
				
					recipe.setPortionsize( Integer.parseInt( payload.get(k).toString() ) );
					r_id++;
					
			} else if ( k.equals("minutes") ) {
					if (recipe.getMinutes() != null)
						return error_message + 3;
				
					recipe.setMinutes(Integer.parseInt(payload.get(k).toString()));
					r_id++;
					
				} else if (k.equals("recipe")) {
					if (recipe.getRecipe() != null)
						return error_message + 4;
				
					recipe.setRecipe(payload.get(k).toString());
					r_id++;
					
				} else if (k.equals("ingredients") ) {
					r_id++;
					
					ArrayList<Ingredients> in = new ArrayList<Ingredients>();
					
					if ( !payload.get(k).getClass().getName().equals("java.util.ArrayList") ) {
						return error_message + 5;
					}
					
					List<Map<String,Object>> list = (ArrayList)payload.get(k);
					Map<String, Object> m;
					
					if (list == null)
						return error_message + 5;
					
					if (recipe.getIngredients() == null)
						recipe.setIngredients(new ArrayList<Ingredients>() );
					
					Ingredients ingredients = new Ingredients();
					for (int i=0; i<list.size(); i++) {
						//teksti += i + ", ";
						m = list.get(i);
						if (m != null && m.keySet() != null && m.keySet().iterator() != null) {
							Iterator<String> it2 = m.keySet().iterator();
							String k2;
							
							i_id = 0;
							while ( it2.hasNext() ) {
																
								k2 = it2.next();
								if (k2.equals("material") ) {
									if (ingredients.getMaterials() != null)
										return error_message + 6;
									
									Long id = Long.parseLong(m.get(k2).toString());
									Materials mat = new Materials();
									mat.setMaterial_id(id);
									ingredients.setMaterials( mat );
									
									i_id++;
								} else if (k2.equals("measure") ) {
									if (ingredients.getMeasurements() != null)
										return error_message + 7;
									
									Long id = Long.parseLong(m.get(k2).toString());
									Measurements mes = new Measurements();
									mes.setMeasure_id(id);
									ingredients.setMeasurements( mes );
									i_id++;
								}  else if (k2.equals("amount") ) {
									if (ingredients.getAmount() != null)
										return error_message + 8;
									
									Double am = Double.parseDouble( m.get(k2).toString() );
									ingredients.setAmount(am);
									i_id++;
								}
								
								
							} // while it2
							
							if (i_id != 3)
								return error_message + 9;
							
							recipe.getIngredients().add(ingredients);
							
					    }
					}
					
				}
				
			}
			
			
			if (r_id != 5)
				return error_message + 10;
			
		} catch (Exception e) {
			return "{\"error\" : 1, \"message\" : \"Validation error\"}";
			//return error_message;
		}
		
		try {
			recipe.setRecipe_id(0L);
			List<Ingredients> list = recipe.getIngredients();
			recipe.setIngredients(null);
			Recipes saved_recipe = recipesRepository.save(recipe);
			
			long id = saved_recipe.getRecipe_id().longValue();
			
			Ingredients in;
			recipe.setRecipe_id(id);
			for (int i=0; i<list.size(); i++) {
				in = list.get(i);
				ingredientsDAO.save(id, in.getAmount().doubleValue(), in.getMeasurements().getMeasure_id() , in.getMaterials().getMaterial_id());
			}
		} catch (Exception e) {
			return "{\"error\":1, \"message\":\"error with db\"}";
		}
		
		
		
		return "{\"success\":1}";
	
	}
	
	
	@RequestMapping(value="/api/recipes", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiChangeRecipies(@RequestBody Map<String, Object> payload) {
			
		long recipe_id = 0;
		String change_type = "";
		long ingredient_id = 0;
		String ingredient_param = "";
		String new_value = "";
		
		try {
			recipe_id = Long.valueOf(payload.get("recipe_id").toString());
			change_type = payload.get("change_type").toString();
			new_value   = payload.get("new_value").toString();
		} catch (Exception e) {
			return "{\"error\":1}";
		}
		
		
		try {
			if (payload.containsKey("ingredient_param"))
				ingredient_param = payload.get("ingredient_param").toString();
		} catch (Exception e) {
			
		}
		
		try {
			if (payload.containsKey("ingredient_id"))
				ingredient_id =  Long.valueOf( payload.get("ingredient_id").toString() );
		} catch (Exception e) {
			
		}
		
		try {
			if (payload.containsKey("ingredient_param"))
				ingredient_param = payload.get("ingredient_param").toString();
		} catch (Exception e) {
			
		}
		
		if (recipe_id < 1 || change_type == null || new_value == null ||
			change_type.trim().equals("") || new_value.trim().equals("") )
			return "{\"error\":1}";
		
			
		try {
			
			Optional<Recipes> o_recipe = recipesRepository.findById(recipe_id);
			
			if (o_recipe.get() == null || o_recipe.get().getRecipe_id().longValue() != recipe_id)
				return "{\"error\":1}";
			
			Recipes recipe = o_recipe.get();
			
			if (change_type.equals("title")) {
				recipe.setTitle(new_value);
				recipesRepository.save(recipe);
				return "{\"success\":1}";
			}
			
			if (change_type.equals("time")) {
				recipe.setMinutes(Integer.parseInt(new_value));
				recipesRepository.save(recipe);
				return "{\"success\":1}";
			}
			
			
			if (change_type.equals("portionsize")) {
				recipe.setPortionsize(Integer.parseInt(new_value));
				recipesRepository.save(recipe);
				return "{\"success\":1}";
			}
			
			
			
			if (change_type.equals("ingredient")) {
				
				if (ingredient_id < 1 || ingredient_param == null || ingredient_param.trim().equals(""))
					return "{\"error\":1}";
				
				if ( ! (ingredient_param.equals("amount") || ingredient_param.equals("measure") || ingredient_param.equals("material")) )
					return "{\"error\":1}";
				
				List<Ingredients> ingredients = recipe.getIngredients();
				
				if (ingredients == null)
					return "{\"error\":1}";
				
				for (int i=0; i<ingredients.size(); i++) {
				
					if (ingredients.get(i).getIngredient_id().longValue() == ingredient_id ) {
						Ingredients in = ingredients.get(i);
						if (ingredient_param.equals("amount") ) {
							in.setAmount(Double.parseDouble(new_value));
							ingredientsDAO.save(in.getIngredient_id(), in.getRecipes().getRecipe_id(), in.getAmount(),
									            in.getMeasurements().getMeasure_id(), in.getMaterials().getMaterial_id());
							return "{\"success\":1}";
						}
						
						
						if (ingredient_param.equals("measure") ) {
							
							Measurements measurement = null;
							try {
								Optional<Measurements> o_m  = measurementsRepository.findById(Long.parseLong(new_value));
								measurement = o_m.get();
							} catch (Exception e) {
								measurement = measurementsRepository.findByMeasure(new_value);
							}
							
							if (measurement != null && measurement.getMeasure_id() > 0) {
								ingredientsDAO.save(in.getIngredient_id(), in.getRecipes().getRecipe_id(), in.getAmount(), 
										            measurement.getMeasure_id() , in.getMaterials().getMaterial_id());
								return "{\"success\":1}";
							}
						}
						
						
						if (ingredient_param.equals("material") ) {
							
							
							Materials material = null;
							try {
								Optional<Materials> o_m  = materialsRepository.findById(Long.parseLong(new_value));
								material = o_m.get();
							} catch (Exception e) {
								material = materialsRepository.findByName(new_value);
							}
							
							if (material != null && material.getMaterial_id()>0 ) {
								ingredientsDAO.save(in.getIngredient_id(), in.getRecipes().getRecipe_id(), in.getAmount(), 
										            in.getMeasurements().getMeasure_id() , material.getMaterial_id());
								return "{\"success\":1}";
							}
						}
						
						
					}
				}
				
				
			}
			
		
		} catch (Exception e) {
			
		}
			
		
		return "{\"error\":1}";
	}
	
	
	
	
	@RequestMapping(value="/api/recipes", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiDeleteRecipies(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long recipe_id =0;
	
		if (payload == null || payload.keySet().isEmpty() )
			return error_message;
		
		try {
			recipe_id = Long.valueOf(payload.get("id").toString());
			
		} catch (Exception e) {
			return error_message;
		}
		
		
		if (recipe_id  <= 0 )
			return error_message;
		
		
		
		
		try {
			Optional<Recipes> list = recipesRepository.findById(recipe_id);
			
			if (list == null || list.get() == null || list.get().getRecipe_id() == null)
				return error_message;
				
			
			ingredientsDAO.deleteByRecipe(recipe_id);
			
			recipesRepository.deleteById(recipe_id);
			
		} catch (Exception e) {
			return "{\"error\":1}";
		}
		
		return "{\"success\":1}";
		
	}
	
	
	
	
	@RequestMapping(value="/api/foodcloset", method = RequestMethod.GET)
	@ResponseBody
	public List<Materials> apiFoodCloset() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		List<Materials> content = closetDAO.getClosetContent(username);
		
		return content;
	}
	
	
	@RequestMapping(value="/api/foodcloset", method = RequestMethod.POST)
	@ResponseBody
	public String apiAddFoodCloset(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long material_id =0;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") )
			return error_message;
		
		try {
			material_id = Long.valueOf(payload.get("id").toString());
			
		} catch (Exception e) {
			return error_message;
		}
		
		if (material_id <1)
			return error_message;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		try {
			closetDAO.save(username, material_id);
		} catch (Exception e) {
			return error_message;
		}
		
		return "{\"success\":1}";
	}
	
	
	@RequestMapping(value="/api/foodcloset", method = RequestMethod.DELETE)
	@ResponseBody
	public String apiDeleteFoodCloset(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long material_id =0;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") )
			return error_message;
		
		try {
			material_id = Long.valueOf(payload.get("id").toString());
			
		} catch (Exception e) {
			return error_message;
		}
		
		
		
		
		if (material_id<1)
			return error_message;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		try {
			closetDAO.delete(username, material_id);
		} catch (Exception e) {
			return "{\"error\":1}";
		}
		
		return "{\"success\":1}";
	}
	
	
	@RequestMapping(value="/api/materials", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public List<Materials> apiMaterials() {

		List<Materials> materials = (List<Materials>) materialsRepository.findAll();
			
		return materials;
	}
	
	
	@RequestMapping(value="/api/materials", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiAddMaterials(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		String material = null;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("material") )
			return error_message;
		
		try {
			material= payload.get("material").toString();
			
		} catch (Exception e) {
			return error_message;
		}
		
		if (material == null || material.trim().equals("") ) 
			return error_message;
		
		try {
			Materials saved = materialsRepository.save(new Materials(material));
			return "{\"success\":1, \"id\":" + saved.getMaterial_id() + "}";
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	@RequestMapping(value="/api/materials", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiDeleteMaterials(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long material_id =0;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") )
			return error_message;
		
		try {
			material_id = Long.valueOf(payload.get("id").toString());
			
		} catch (Exception e) {
			return error_message;
		}
		
		if (material_id<1 ) 
			return error_message;
		
		try {
			
			materialsRepository.deleteById(material_id);
			return "{\"success\":1}";
			
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	
	@RequestMapping(value="/api/materials", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiChangeMaterials(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long material_id =0;
		String material = null;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") || !payload.containsKey("material"))
			return error_message;
		
		try {
			material_id = Long.valueOf(payload.get("id").toString());
			material = payload.get("material").toString();
			
		} catch (Exception e) {
			return error_message;
		}

		
		if (material_id<1 || material == null || material.trim().equals("")  ) 
			return error_message;
		
		try {
			materialsRepository.save(new Materials(material_id, material));
			return "{\"success\":1}";
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	
	
	@RequestMapping(value="/api/measurements", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public List<Measurements> apiMeasurements() {

		List<Measurements> measurements = (List<Measurements>) measurementsRepository.findAll();
			
		return measurements;
	}
	
	
	@RequestMapping(value="/api/measurements", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiAddMeasurements(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		String measurement = null;
		String short_text = null;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("measurement") || !payload.containsKey("short") )
			return error_message;
		
		try {
			measurement = payload.get("measurement").toString();
			short_text = payload.get("short").toString();
			
		} catch (Exception e) {
			return error_message;
		}
			
		
		if (measurement == null || short_text == null || measurement.trim().equals("") || short_text.trim().equals("") ) 
			return error_message;
		
		try {
			Measurements saved = measurementsRepository.save(new Measurements(measurement, short_text));
			return "{\"success\":1, \"id\":" + saved.getMeasure_id() + "}";
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	@RequestMapping(value="/api/measurements", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiDeleteMeasurements(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long measurement_id =0;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") )
			return error_message;
		
		try {
			measurement_id = Long.valueOf(payload.get("id").toString());
			
		} catch (Exception e) {
			return error_message;
		}
			
			
		
		if ( measurement_id<1 ) 
			return error_message;
		
		try {
			
			measurementsRepository.deleteById(measurement_id);
			return "{\"success\":1}";
			
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	
	@RequestMapping(value="/api/measurements", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	public String apiChangeMeasurements(@RequestBody Map<String, Object> payload) {
		
		String error_message = "{\"error\":1}";
		long measurement_id =0;
		String measurement = null;
		String short_text = null;
	
		if (payload == null || payload.isEmpty() || !payload.containsKey("id") || !payload.containsKey("measurement") || !payload.containsKey("short") )
			return error_message;
		
		try {
			measurement_id = Long.valueOf(payload.get("id").toString());
			measurement = payload.get("measurement").toString();
			short_text = payload.get("short").toString();
			
		} catch (Exception e) {
			return error_message;
		}
			

		
		if (measurement_id <1 || measurement == null || short_text == null || 
		    measurement.trim().equals("") || short_text.trim().equals("")  ) 
			return error_message;
		
		try {
			measurementsRepository.save(new Measurements(measurement_id, measurement, short_text));
			return "{\"success\":1}";
		} catch (Exception e) {
			
		}
		
			
		return error_message;
	}
	
	
	
}
