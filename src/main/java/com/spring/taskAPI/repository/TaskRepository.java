package com.spring.taskAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
	Page<Task> findByStatusContainingIgnoreCase(Status status, Pageable pageable);
	Page<Task> findByPriorityContainingIgnoreCase(Priority priority, Pageable pageable);

}
