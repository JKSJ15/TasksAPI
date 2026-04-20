package com.spring.taskAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService service;
	public AuthController(AuthService service) {
		super();
		this.service = service;
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto login) {
		return ResponseEntity.ok(service.login(login));
	}
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody LoginDto login) {
		service.register(login);
		return ResponseEntity.ok().build();
	}
}
