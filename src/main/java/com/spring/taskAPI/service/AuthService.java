package com.spring.taskAPI.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.taskAPI.configurations.JwtService;
import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class AuthService {
	private final UserRepository rep;
	private final AuthenticationManager authMan;
	private final PasswordEncoder encoder;
	private final JwtService jwt;
	public AuthService(UserRepository rep, AuthenticationManager authMan, PasswordEncoder encoder, JwtService jwt) {
		super();
		this.rep = rep;
		this.authMan = authMan;
		this.encoder = encoder;
		this.jwt = jwt;
	}


	public void register(LoginDto login) {
		Optional<User> find = rep.findByLogin(login.login());
		if(find.isPresent()) {throw new InvalidAtributeException("User already exists!");}
		String password = encoder.encode(login.password());
		User user = new User(login.login(), password);
		rep.save(user);
	}
	public String login(LoginDto login) {
		var authtoke = new UsernamePasswordAuthenticationToken(login.login(), login.password());
		var auth = authMan.authenticate(authtoke);
		User user =(User) auth.getPrincipal();
		return jwt.generateToken(user);
	}
}
