package com.spring.taskAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.dto.LoginResponseDto;
import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService service;

	public AuthController(AuthService service) {
		this.service = service;
	}

	@Operation(summary = "User authentication", description = "Returns a JWT Bearer token. Use it in the Authorization header as: Bearer <token>")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PostMapping("/login")
	public ResponseEntity<String> login(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User credentials") @RequestBody LoginDto login) {
		return ResponseEntity.ok(service.login(login));
	}

	@Operation(summary = "User registration", description = "Register a new user")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "User successfully registered"),
			@ApiResponse(responseCode = "400", description = "User already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PostMapping("/register")
	public ResponseEntity<Void> register(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data") @RequestBody LoginDto login) {
		service.register(login);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
