package com.spring.taskAPI.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.taskAPI.configurations.JwtService;
import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.dto.TokenRefreshRequestDto;
import com.spring.taskAPI.dto.TokenRefreshResponseDto;
import com.spring.taskAPI.entity.RefreshToken;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.InvalidRefreshTokenException;
import com.spring.taskAPI.exception.UserNotFoundException;
import com.spring.taskAPI.repository.TokenRefreshRepository;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private final UserRepository rep;
	private final AuthenticationManager authMan;
	private final PasswordEncoder encoder;
	private final JwtService jwt;
	private final TokenRefreshService refreshService;
	private final TokenRefreshRepository refreshRepository;

	public AuthService(UserRepository rep, AuthenticationManager authMan, PasswordEncoder encoder, JwtService jwt,
			TokenRefreshService refreshService, TokenRefreshRepository refreshRepository) {
		super();
		this.rep = rep;
		this.authMan = authMan;
		this.encoder = encoder;
		this.jwt = jwt;
		this.refreshService = refreshService;
		this.refreshRepository = refreshRepository;
	}

	public void register(LoginDto login) {
		if (rep.findByLogin(login.login()).isPresent()) {
			throw new InvalidAtributeException("User already exists!");
		}
		String password = encoder.encode(login.password());
		User user = new User(login.login(), password);
		rep.save(user);
	}

	public TokenRefreshResponseDto login(LoginDto login) {
		var authToken = new UsernamePasswordAuthenticationToken(login.login(), login.password());
		authMan.authenticate(authToken);
		User user = rep.findByLogin(login.login()).orElseThrow(() -> new UserNotFoundException("User not found"));
		String refresh = jwt.generateRefreshToken(user);
		String access = jwt.generateToken(user);
		refreshService.createRefreshToken(user, refresh);

		logger.info("User {} authenticated successfully", user.getLogin());
		return new TokenRefreshResponseDto(access, refresh);
	}

	public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto request) {
		String refreshToken = request.refreshToken();
		if (!jwt.validRefreshToken(refreshToken)) {
			throw new InvalidRefreshTokenException("Invalid refresh token");
		}
		RefreshToken storedToken = refreshService.validate(refreshToken);
		User user = storedToken.getUser();
		rep.findById(storedToken.getUser().getId())
				.orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
		refreshRepository.delete(storedToken);

		String newAccessToken = jwt.generateToken(user);
		String newRefreshToken = jwt.generateRefreshToken(user);
		refreshService.createRefreshToken(user, newRefreshToken);

		logger.info("Refresh successfully completed");
		return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
	}
}
