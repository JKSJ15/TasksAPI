package com.spring.taskAPI.service;

import org.springframework.stereotype.Service;

import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.repository.TokenRefreshRepository;
import com.spring.taskAPI.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenRefreshRepository refreshTokenRepository;

    public UserService(UserRepository userRepository, TokenRefreshRepository refreshTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
	}
    @Transactional
	public void deleteUser(User user) {
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
