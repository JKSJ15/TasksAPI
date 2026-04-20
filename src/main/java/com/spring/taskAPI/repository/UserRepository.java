package com.spring.taskAPI.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.taskAPI.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByLogin(String login);
}
