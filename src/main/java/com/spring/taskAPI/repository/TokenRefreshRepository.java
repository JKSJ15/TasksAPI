package com.spring.taskAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.spring.taskAPI.entity.RefreshToken;
import com.spring.taskAPI.entity.User;

import jakarta.transaction.Transactional;

public interface TokenRefreshRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);

	@Modifying
	@Transactional
	void deleteByUser(User user);
}
