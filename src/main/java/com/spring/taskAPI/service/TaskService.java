package com.spring.taskAPI.service;

import java.time.LocalDate;

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
import com.spring.taskAPI.mapper.TaskMapper;
import com.spring.taskAPI.repository.TaskRepository;
import com.spring.taskAPI.repository.UserRepository;

@Service
public class TaskService {
	private final TaskRepository tr;
	private final UserRepository ur;

	public TaskService(TaskRepository tr, UserRepository ur) {
		super();
		this.tr = tr;
		this.ur = ur;
	}
	public Page<TaskDto> list(String title, Status status, Priority priority, Pageable pageable){
		Page<Task> tasks;
		String login = getLoggedUser();
		if (title!=null) {
			tasks = tr.findByTitleContainingIgnoreCaseAndUserLogin(title,login, pageable);
		}else if (status!=null) {
			tasks = tr.findByStatusAndUserLogin(status,login, pageable);
		}else if (priority!=null) {
			tasks = tr.findByPriorityAndUserLogin(priority,login, pageable);
		}else {
		        tasks = tr.findByUserLogin(login, pageable);
		}
		return tasks.map(TaskMapper::toDto);
	}
	public TaskDto findById(long id){
	        String login = getLoggedUser();

	        Task task = tr.findByIdAndUserLogin(id, login).orElseThrow(()-> new TaskNotFoundException("Task Not Found!"));
	        return TaskMapper.toDto(task);
	}
	public TaskDto save(TaskDto dto) {
		if (dto.getDeadline().isBefore(LocalDate.now())) {
			throw new InvalidAtributeException("Deadline cannot be in the past");
		}
		Task task = TaskMapper.toTask(dto);
		if (task.getStatus() == Status.COMPLETED) {
			task.setDateConcl(LocalDate.now());
		}
	        String login = getLoggedUser();
	        User user = ur.findByLogin(login)
	                .orElseThrow();
	        task.setUser(user);
	        
	        Task taskSaved= tr.save(task);
	        return TaskMapper.toDto(taskSaved);
	}
	public TaskDto update(Long id, TaskDto dto) {
	    if (dto.getDeadline().isBefore(LocalDate.now())) {
	        throw new InvalidAtributeException("Deadline cannot be in the past");
	    }
	    if (!dto.getId().equals(id)) {
	        throw new InvalidAtributeException("IDs of insert and saved don't combined!");
	    }
	    String login = getLoggedUser();
	    Task task = tr.findByIdAndUserLogin(id, login)
	            .orElseThrow(() -> new TaskNotFoundException("Task not found!"));
	    
	    task.setTitle(dto.getTitle());
	    task.setDescription(dto.getDescription());
	    task.setPriority(dto.getPriority());
	    task.setStatus(dto.getStatus());
	    task.setDeadline(dto.getDeadline());
	    if (task.getStatus() == Status.COMPLETED) {
	        task.setDateConcl(LocalDate.now());
	    }
	    tr.save(task);
	    return TaskMapper.toDto(task);
	}
	public void delete(long id) {
	    String login = getLoggedUser();
	    Task taskToBeDelete = tr.findByIdAndUserLogin(id, login)
	            .orElseThrow(() -> new TaskNotFoundException("Task not found!"));
	    tr.delete(taskToBeDelete);
	}
	
	private String getLoggedUser() {
	    return SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();
	}
}
