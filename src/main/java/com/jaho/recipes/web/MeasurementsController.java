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
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.Measurements;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.UserRepository;

@Controller
public class MeasurementsController {

	
	@Autowired MeasurementsRepository measurementsRepository;
	
	private static final Logger log = LoggerFactory.getLogger(RecipesApplication.class);
	
	private String errors = "";
	private String notices = "";
	private String form = "";
	
	
	 @RequestMapping("/measurements")
	 @PreAuthorize("hasAuthority('ADMIN')")
		public void measurements(Model model) {
			
			
			
			model.addAttribute("errors", this.errors);
			model.addAttribute("notices", this.notices);
			model.addAttribute("addmeasure", new Measurements() );
			model.addAttribute("measurements", measurementsRepository.findAllWithCustomOrderBy(Sort.by(Sort.Direction.ASC, "measure")));
			
			errors = "";
			notices = "";
		}
	
	
	 @RequestMapping(value="/addmeasure", method = RequestMethod.POST)
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public String saveMeasurements(@Valid Measurements measurements, BindingResult bindingResult) {
		 
		 if (bindingResult.hasErrors() || measurements == null || measurements.getMeasure() == null ||
		     measurements.getMeasure().trim().equals("")) {
			 this.errors = "Validation error(s)!";
			 this.notices = "";
			 return "redirect:measurements";
			 
		 }
		 
		 
		 Measurements rs = measurementsRepository.findByMeasure(measurements.getMeasure());
		 
		 if (rs != null) {
			 this.errors = "Validation error(s)!";
			 this.notices = "";
			 return "redirect:measurements";
		 }
		 
		 try {
			 
			 measurementsRepository.save(measurements);
			 
		 } catch(Exception e) {
			 this.errors = "" + e.getMessage();
			 this.notices = "";
			 return "redirect:measurements";
			 
		 }
		 
		 
		 this.errors = "";
		 this.notices = "Measurements were saved succesfully!";
		 return "redirect:measurements";
	 }
	 
	 
	 
	 @RequestMapping(value = "/delete_measurement/{id}", method = RequestMethod.GET)
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public String deleteMeasurement(@PathVariable("id") Long measure_id) {
		 
		if (measure_id == null || measure_id.longValue()<=0) {
			this.errors = "Not valid id value!";
			this.notices = "";
			return "redirect:../measurements";
		}
		 
		try {
			
			measurementsRepository.deleteById(measure_id);
			
		} catch (Exception e) {
			this.errors = e.getMessage();
			this.notices = "";
			return "redirect:../measurements";
		}
		

		
		this.errors = "";
		this.notices = "Measurements were deleted succesfully!";
		return "redirect:../measurements";
		
	 }
	 
	 
	 
	//@PreAuthorize("hasAuthority('ADMIN')")
		 @RequestMapping(value = "/edit_measurement/{id}", method = RequestMethod.GET)
		 @PreAuthorize("hasAuthority('ADMIN')")
		public String editMeasure(@PathVariable("id") Long measure_id, Model model) {
			 
			 if (measure_id==null  || measure_id.longValue()<=0) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../measurements";
			 }
			 
			 
			 Optional<Measurements> measurement = measurementsRepository.findById(measure_id);
			 if ( measurement == null || measurement.get() == null) {
				 this.errors = "Not valid id value!";
				 this.notices = "";
				 return "redirect:../measurements";
			 }
			 
			 
			 
			 model.addAttribute("errors", this.errors);
			 model.addAttribute("measurement", measurement.get());
			 
			 return "edit_measurement";
			 
		 }
	 
	 

		 @RequestMapping(value = "/edit_measurement", method = RequestMethod.POST)
		 @PreAuthorize("hasAuthority('ADMIN')")
		 //@PreAuthorize("hasAuthority('ADMIN')")
		 public String saveEditMeasure(@Valid Measurements measurement, BindingResult bindingResult){
			 
			 if (bindingResult.hasErrors() || measurement == null) {
		    		this.errors = "Validation error(s). ";
		    		this.notices = "";
		    		
		    		if (measurement == null || measurement.getMeasure_id() == null)
		    			return "redirect:../measurements";
		    					
		    		return "redirect:/edit_measurement/" + measurement.getMeasure_id().longValue();
		    }
			 
			try {
				
				measurementsRepository.save(measurement);
				
			} catch (Exception e) {
				
				this.notices = "";
				this.errors = "" + e.getMessage();
				return "redirect:/edit_measurement/" + measurement.getMeasure_id().longValue();
			}
			 
			 
			 
			this.notices = "Changes were saved succesfully!";
			this.errors = "";
			return "redirect:/measurements";
			 
			 
		 }
	
}
