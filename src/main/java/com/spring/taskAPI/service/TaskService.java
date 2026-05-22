package com.spring.taskAPI.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.TaskNotFoundException;
import com.spring.taskAPI.exception.UserNotFoundException;
import com.spring.taskAPI.mapper.TaskMapper;
import com.spring.taskAPI.repository.TaskRepository;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class TaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository tr;
	private final UserRepository ur;

	public TaskService(TaskRepository tr, UserRepository ur) {
		super();
		this.tr = tr;
		this.ur = ur;
	}

	public Page<TaskDto> list(String title, Status status, Priority priority, Pageable pageable) {
		Page<Task> tasks;
		String login = getLoggedUser();
		if (title != null) {
			logger.info("Listing all with title {}", title);
			tasks = tr.findByTitleContainingIgnoreCaseAndUserLogin(title, login, pageable);
		} else if (status != null) {
			logger.info("Listing tasks with status {} {}", status);
			tasks = tr.findByStatusAndUserLogin(status, login, pageable);
		} else if (priority != null) {
			logger.info("Listing all tasks with priority {}", priority);
			tasks = tr.findByPriorityAndUserLogin(priority, login, pageable);
		} else {
			logger.info("Listing all tasks");
			tasks = tr.findByUserLogin(login, pageable);
		}
		return tasks.map(TaskMapper::toDto);
	}

	public TaskDto findById(long id) {
		String login = getLoggedUser();
		Task task = tr.findByIdAndUserLogin(id, login).orElseThrow(() -> new TaskNotFoundException("Task Not Found!"));
		logger.info("Task {} found", id);
		return TaskMapper.toDto(task);
	}

	public TaskDto save(TaskDto dto) {
		validateDates(dto);
		Task task = TaskMapper.toTask(dto);
		processTaskStatus(task);
		String login = getLoggedUser();
		logger.info("Creating task for user {}", login);
		User user = ur.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found!"));
		task.setUser(user);
		Task taskSaved = tr.save(task);
		logger.info("Task {} created successfully", taskSaved.getId());
		return TaskMapper.toDto(taskSaved);
	}

	public TaskDto update(Long id, TaskDto dto) {
		validateDates(dto);
		if (!id.equals(dto.getId())) {
			logger.warn("Path ID {} does not match body ID {}", id, dto.getId());
			throw new InvalidAtributeException("Path ID does not match request body ID!");
		}
		String login = getLoggedUser();
		logger.info("Updating task for user {}", login);
		Task task = tr.findByIdAndUserLogin(id, login).orElseThrow(() -> new TaskNotFoundException("Task not found!"));
		task.setTitle(dto.getTitle());
		task.setDescription(dto.getDescription());
		task.setPriority(dto.getPriority());
		task.setStatus(dto.getStatus());
		task.setDeadline(dto.getDeadline());
		processTaskStatus(task);
		Task updated = tr.save(task);
		logger.info("Task {} updated successfully", updated.getId());
		return TaskMapper.toDto(updated);
	}

	public void delete(long id) {
		String login = getLoggedUser();
		Task taskToBeDelete = tr.findByIdAndUserLogin(id, login)
				.orElseThrow(() -> new TaskNotFoundException("Task not found!"));
		logger.info("Task {} found", taskToBeDelete.getId());
		tr.delete(taskToBeDelete);
	}

	private String getLoggedUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private void validateDates(TaskDto dto) {
		LocalDate today = LocalDate.now();
		if (dto.getDeadline().isBefore(today)) {
			throw new InvalidAtributeException("Deadline cannot be in the past");
		}
		if (dto.getDateConcl() != null && dto.getDateConcl().isAfter(today)) {
			throw new InvalidAtributeException("DateConcl cannot be in the future");
		}
	}

	private void processTaskStatus(Task task) {
		if (task.getStatus() == Status.COMPLETED) {
			task.setDateConcl(LocalDate.now());
		} else {
			task.setDateConcl(null);
		}
	}
}
