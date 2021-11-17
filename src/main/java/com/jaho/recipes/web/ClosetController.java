package com.jaho.recipes.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jaho.recipes.RecipesApplication;
import com.jaho.recipes.domain.ClosetDAO;
import com.jaho.recipes.domain.IngredientsDAO;
import com.jaho.recipes.domain.Materials;
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.UserRepository;

@Controller
public class ClosetController {
	
	@Autowired MaterialsRepository materialsRepository;
	@Autowired MeasurementsRepository measurementsRepository;
	@Autowired RecipesRepository recipesRepository;
	@Autowired UserRepository userRepository;
	@Autowired IngredientsDAO ingredientsDAO;
	@Autowired ClosetDAO closetDAO;
	
	//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	private static final Logger log = LoggerFactory.getLogger(RecipesApplication.class);
	
	private String errors = "";
	private String notices = "";
	
	@RequestMapping("/foodcloset")
	public void foodCloset(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		model.addAttribute("errors", this.errors);
		model.addAttribute("notices", this.notices);
		model.addAttribute("materials", closetDAO.getClosetContent(username) );
		
		
		this.errors = "";
		this.notices = "";
		
		
	}

	
	@RequestMapping(value="/add_foodcloset", method = RequestMethod.GET)
	public void addFoodCloset(Model model) {
		
		
		model.addAttribute("errors", this.errors);
		model.addAttribute("notices", this.notices);
		model.addAttribute("materials", materialsRepository.findAllWithCustomOrderBy(Sort.by(Sort.Direction.ASC, "name")));
		
		
		this.errors = "";
		this.notices = "";
		
	}
	
	
	
	 @RequestMapping(value = "/add_foodcloset", method = RequestMethod.POST)
	 public String saveFoodCloset(@RequestBody String postBody) {
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		 
		 if (postBody == null || postBody.trim().equals("") ) {
			 
			 this.errors = "Validation error(s)!";
			 this.notices = "";
			 
			
			 return "redirect:/add_foodcloset";
		 }
		 
		 
		 String[] params = postBody.split("&");
		 String[] help;
		 String str;

		 
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
				 this.notices = "" + e.getMessage();
				 
				
				 return "redirect:/add_foodcloset";
	     }
		 
		 
		 
		 String text = "";
		 int index;
		 Long value;
		 try {
			 for (int i=1; i<=10; i++) {
				 
				 index = p.indexOf( "addmaterial[" + i + "]" );
				 
				 
				 if (index>=0) {
					 text += i + " - ";
					 value = Long.parseLong( v.get(index) );
					 text += value;
					 if (value>0)
						 text += "  " + closetDAO.save(username, value.longValue() );	
					 
					 text += "<br>"; 
				 }
			 }
			 
		 } catch (Exception e) {
			 this.errors = "There were error when adding materials!" + text;
			 this.notices = "" + text + " " + e.getMessage();
			 
			
			 return "redirect:/add_foodcloset";

		 }
		 
		 
		 this.notices ="Materials were succesfully added to food closet!";
		 this.errors = "" + text;
		 
		 return "redirect:add_foodcloset";
	 }
	 
	 
	 
	 @RequestMapping(value = "/delete_foodcloset/{id}", method = RequestMethod.GET)
	 public String deleteFoodCLoset(@PathVariable("id") Long material_id) {
		 
		if (material_id == null || material_id.longValue()<=0) {
			this.errors = "Not valid id value!";
			this.notices = "";
			return "redirect:../materials";
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		try {
		
			closetDAO.delete(username, material_id.longValue());
			
		} catch (Exception e) {
			this.errors = e.getMessage();
			this.notices = "";
			return "redirect:../foodcloset";
		}
		
		
			
		this.errors = "";
		this.notices = "Material were deleted succesfully!";
		return "redirect:../foodcloset";
		
	 }
	 
	 
}
