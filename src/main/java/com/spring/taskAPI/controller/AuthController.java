package com.spring.taskAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.dto.TokenRefreshRequestDto;
import com.spring.taskAPI.dto.TokenRefreshResponseDto;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.service.AuthService;
import com.spring.taskAPI.service.TokenRefreshService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final AuthService service;
	private final TokenRefreshService refreshService;

	public AuthController(AuthService service, TokenRefreshService refreshService) {
		super();
		this.service = service;
		this.refreshService = refreshService;
	}

	@Operation(summary = "User authentication", description = "Returns a JWT Bearer token. Use it in the Authorization header as: Bearer <token>")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponseDto.class))),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PostMapping("/login")
	public ResponseEntity<TokenRefreshResponseDto> login(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User credentials") @RequestBody @Valid LoginDto login) {
		
		logger.info("Login request received for user: {}", login.login());
		return ResponseEntity.ok(service.login(login));
		
	}

	@Operation(summary = "User registration", description = "Register a new user")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "User successfully registered"),
			@ApiResponse(responseCode = "400", description = "User already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "401", description = "Invalid credentials") })
	@PostMapping("/register")
	public ResponseEntity<Void> register(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data") @RequestBody @Valid LoginDto login) {
		
		logger.info("Register request for user: {}", login.login());
		service.register(login);
		logger.info("User registered successfully: {}", login.login());
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}

	@Operation(summary = "Refresh token", description = "Generate a new refresh and acess token")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Generate successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "No body/token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "401", description = "Invalid refresh token or expired token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PostMapping("/refresh")
	public ResponseEntity<TokenRefreshResponseDto> refresh(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User refresh token") @RequestBody @Valid TokenRefreshRequestDto request) {
		
		logger.info("Refresh token request received");
		return ResponseEntity.ok(service.refreshToken(request));
		
	}

	@Operation(summary = "Logout")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Logout successfully"),
			@ApiResponse(responseCode = "401", description = "Invalid access token/expired token") })
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(Authentication authentication) {
		
		if (authentication == null) {
			logger.warn("Logout failed: authentication is null");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		User user = (User) authentication.getPrincipal();
		logger.info("Logout request for user: {}", user.getLogin());
		refreshService.deleteByUser(user);
		logger.info("Refresh tokens deleted for user: {}", user.getLogin());
		return ResponseEntity.noContent().build();
		
	}
}
