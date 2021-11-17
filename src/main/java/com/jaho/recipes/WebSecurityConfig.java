package com.jaho.recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
*/
import com.jaho.recipes.web.UserDetailServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {
	 @Autowired
	 private UserDetailServiceImpl userDetailsService;	
	 
	 
	 protected void configure(HttpSecurity http) throws Exception {
		 
	        http 
	        .authorizeRequests()
	        .antMatchers( "/api/**", "/css/*").permitAll() // Enable css when logged out
	        .anyRequest().permitAll()
	        
	         //.and()
	         //.antMatchers("/api/**").permitAll() 
	        //.antMatchers("/admin/**").hasRole("ADMIN");
	        
	        .and()
	        .authorizeRequests()
	        .anyRequest().authenticated()
	         
	        .and()
	        .formLogin()
	            .defaultSuccessUrl("/recipes", true)
	            .permitAll()
	        .and()
	        .logout()
	            .permitAll();
	        	
	    }
	 
	 
	    
	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	    }
	 
	 
}
