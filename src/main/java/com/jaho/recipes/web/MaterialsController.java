package com.jaho.recipes.web;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jaho.recipes.RecipesApplication;
import com.jaho.recipes.domain.ClosetDAO;
import com.jaho.recipes.domain.IngredientsDAO;
import com.jaho.recipes.domain.Materials;
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.UserRepository;

@Controller
public class MaterialsController {
	
	@Autowired MaterialsRepository materialsRepository;
	
	private static final Logger log = LoggerFactory.getLogger(RecipesApplication.class);
	
	private String errors = "";
	private String notices = "";
	private String form = "";
	
	@RequestMapping("/materials")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void materials(Model model) {
		
		
		model.addAttribute("addmaterial", new Materials());
		model.addAttribute("errors", this.errors);
		model.addAttribute("notices", this.notices);
		model.addAttribute("materials", materialsRepository.findAllWithCustomOrderBy(Sort.by(Sort.Direction.ASC, "name")));
		
		errors = "";
		notices = "";
	}
	
	 @RequestMapping(value="/addmaterial", method=RequestMethod.POST)
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public String saveMaterial(@Valid Materials material, BindingResult bindingResult) {
		 
		 if (bindingResult.hasErrors() || material == null || material.getName() == null ||
			     material.getName().trim().equals("")) {
				 this.errors = "Validation error(s)!";
				 this.notices = "";
				 return "redirect:materials";
				 
			 }
			 
			 
			 Materials rs = materialsRepository.findByName(material.getName());
			 
			 if (rs != null) {
				 this.errors = "Validation error(s)!";
				 this.notices = "";
				 return "redirect:materials";
			 }
		 
		 
		 
		 try {

			 materialsRepository.save(material);
			 
		 } catch(Exception e) {
			 this.notices = "";
	    	 this.errors = e.getMessage();
	    	 return "redirect:materials";
		 }
		 
		 this.errors = "";
		 this.notices = "Material were saved succesfully!";
		 return "redirect:materials";
		 
		 
		 
	 }
	 
	 @RequestMapping(value = "/delete_material/{id}", method = RequestMethod.GET)
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public String deleteMaterial(@PathVariable("id") Long material_id) {
		 
		if (material_id == null || material_id.longValue()<=0) {
			this.errors = "Not valid id value!";
			this.notices = "";
			return "redirect:../materials";
		}
		 
		try {
			materialsRepository.deleteById(material_id);
		} catch (Exception e) {
			this.errors = e.getMessage();
			this.notices = "";
			return "redirect:../materials";
		}
		
		
			
		this.errors = "";
		this.notices = "Material were deleted succesfully!";
		return "redirect:../materials";
		
	 }
	 
	 
	
	 @RequestMapping(value = "/edit_material/{id}", method = RequestMethod.GET)
	 @PreAuthorize("hasAuthority('ADMIN')")
	public String editMaterial(@PathVariable("id") Long material_id, Model model) {
		 
		 if (material_id==null  || material_id.longValue()<=0) {
			 this.errors = "Not valid id value!";
			 this.notices = "";
			 return "redirect:../materials";
		 }
		 
		 
		 Optional<Materials> material = materialsRepository.findById(material_id);
		 if ( material == null || material.get() == null) {
			 this.errors = "Not valid id value!";
			 this.notices = "";
			 return "redirect:../materials";
		 }
		 
		 
		 
		 model.addAttribute("errors", this.errors);
		 model.addAttribute("material", material.get());
		 
		 return "edit_material";
		 
	 }
	 
	 
	 
	 
	 @RequestMapping(value = "/edit_material", method = RequestMethod.POST)
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public String saveEditMaterial(@Valid Materials material, BindingResult bindingResult){
		 
		 if (bindingResult.hasErrors() || material == null) {
	    		this.errors = "Validation error(s). ";
	    		this.notices = "";
	    		
	    		if (material == null || material.getMaterial_id() == null)
	    			return "redirect:../materials";
	    					
	    		return "redirect:/edit_material/" + material.getMaterial_id().longValue();
	    }
		 
		try {
			
			materialsRepository.save(material);
			
		} catch (Exception e) {
			
			this.notices = "";
			this.errors = "" + e.getMessage();
			return "redirect:/edit_material/" + material.getMaterial_id().longValue();
		}
		 
		 
		 
		this.notices = "Changes were saved succesfully!";
		this.errors = "";
		return "redirect:/materials";
		 
		 
	 }
	
}
