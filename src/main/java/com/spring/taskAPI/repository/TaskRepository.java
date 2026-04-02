package com.spring.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.taskAPI.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
