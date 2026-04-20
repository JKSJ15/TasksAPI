package com.spring.taskAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	Page<Task> findByTitleContainingIgnoreCaseAndUserLogin(String title,String login, Pageable pageable);
	Page<Task> findByStatusAndUserLogin(Status status,String login, Pageable pageable);
	Page<Task> findByPriorityAndUserLogin(Priority priority,String login, Pageable pageable);
	Page<Task> findByUserLogin(String login, Pageable pageable);
    Optional<Task> findByIdAndUserLogin(Long id, String login);
}
