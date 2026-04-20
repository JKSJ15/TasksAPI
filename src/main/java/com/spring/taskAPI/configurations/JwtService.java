package com.spring.taskAPI.configurations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.taskAPI.entity.User;

@Service
public class JwtService {
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			Algorithm augorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
			.withExpiresAt(generateExpireTime())
			.withIssuer("tasksAPI")
			.withSubject(user.getLogin())
			.sign(augorithm);
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Error while generation JWT token "+e);
		}
	}
	public String validToken(String token) {
		try {
			Algorithm augorithm = Algorithm.HMAC256(secret);
			String ValidatedToken = JWT.require(augorithm)
			.withIssuer("tasksAPI")
			.build()
			.verify(token)
			.getSubject();
			return ValidatedToken;
		} catch (JWTVerificationException e) {
			return null;
		}
		
	}
	private Instant generateExpireTime() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
