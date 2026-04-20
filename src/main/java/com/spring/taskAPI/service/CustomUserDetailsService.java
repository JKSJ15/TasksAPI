package com.spring.taskAPI.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.UserNotFoundException;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository rep;
	public CustomUserDetailsService(UserRepository rep) {
		super();
		this.rep = rep;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = rep.findByLogin(username).orElseThrow(); 
		return user;
	}
	
}
