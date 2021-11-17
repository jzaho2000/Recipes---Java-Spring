package com.jaho.recipes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jaho.recipes.domain.User;
import com.jaho.recipes.domain.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserDetailServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
			User curruser = userRepository.findByUsername(username);
			
			UserDetails user = new org.springframework.security.core.userdetails.User(username,
			curruser.getPassword(),
			
			AuthorityUtils.createAuthorityList(curruser.getRole()));
			return user;
	}
}
