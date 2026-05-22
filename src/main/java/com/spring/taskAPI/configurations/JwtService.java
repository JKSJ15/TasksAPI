package com.spring.taskAPI.configurations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.taskAPI.entity.User;

@Service
public class JwtService {
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	private final Algorithm algorithm;

	public JwtService(@Value("${api.security.token.secret}") String secret) {
		this.algorithm = Algorithm.HMAC256(secret);
	}

	public String generateToken(User user) {
		logger.debug("Generating access token for user {}", user.getLogin());
		try {
			String token = JWT.create().withExpiresAt(generateExpireTime()).withIssuer("tasksAPI")
					.withSubject(user.getLogin()).sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			logger.error("Error generating JWT token", e);
			throw new RuntimeException("Error while generation JWT token ");
		}
	}

	public String generateRefreshToken(User user) {
		logger.debug("Generating refresh token for user {}", user.getLogin());
		try {
			String refreshToken = JWT.create().withJWTId(UUID.randomUUID().toString())
					.withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))).withIssuer("tasksAPI")
					.withIssuedAt(new Date()).withSubject(user.getLogin()).sign(algorithm);
			return refreshToken;
		} catch (JWTCreationException e) {
			logger.error("Error generating JWT token", e);
			throw new RuntimeException("Error while generation JWT refresh token ");
		}
	}

	public String validToken(String token) {
		try {
			String validatedToken = JWT.require(algorithm).withIssuer("tasksAPI").build().verify(token).getSubject();
			return validatedToken;
		} catch (JWTVerificationException e) {
			logger.warn("Invalid or expired JWT token");
			return null;
		}

	}

	public boolean validRefreshToken(String token) {
		try {
			JWT.require(algorithm).withIssuer("tasksAPI").build().verify(token).getSubject();
			return true;
		} catch (JWTVerificationException e) {
			logger.warn("Invalid or expired refresh token");
			return false;
		}

	}

	public String extractUserLogin(String token) {
		return JWT.require(algorithm).withIssuer("tasksAPI").build().verify(token).getSubject();
	}

	private Instant generateExpireTime() {
		return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
	}
}
