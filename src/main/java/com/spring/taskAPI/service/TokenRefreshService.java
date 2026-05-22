package com.spring.taskAPI.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.spring.taskAPI.entity.RefreshToken;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.InvalidRefreshTokenException;
import com.spring.taskAPI.exception.TokenWasExpiredException;
import com.spring.taskAPI.repository.TokenRefreshRepository;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class TokenRefreshService {
	private final TokenRefreshRepository tokenRepository;

	public TokenRefreshService(TokenRefreshRepository tokenRepository, UserRepository ur) {
		super();
		this.tokenRepository = tokenRepository;
	}

	public RefreshToken createRefreshToken(User user, String rToken) {
		RefreshToken token = RefreshToken.builder().withRefreshToken(rToken)
				.withExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS)).withUser(user).build();
		return tokenRepository.save(token);
	}

	public RefreshToken validate(String refreshToken) {
		RefreshToken token = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new InvalidRefreshTokenException("Refresh Token Not Found"));
		if (token.isExpired(token.getExpiresAt())) {
			throw new TokenWasExpiredException("token refresh expired");
		}
		return token;
	}

	public void deleteByUser(User user) {
		tokenRepository.deleteByUser(user);
	}

}
