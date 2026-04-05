package com.spring.taskAPI.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.TaskNotFoundException;
import com.spring.taskAPI.mapper.TaskMapper;
import com.spring.taskAPI.repository.TaskRepository;

@Service
public class TaskService {
	private final TaskRepository tr;

	public TaskService(TaskRepository tr) {
		super();
		this.tr = tr;
	}
	
	public Page<TaskDto> list(String title, Status status, Priority priority, Pageable pageable){
		Page<Task> tasks;
		if (title!=null) {
			tasks = tr.findByTitleContainingIgnoreCase(title, pageable);
		}else if (status!=null) {
			tasks = tr.findByStatusContainingIgnoreCase(status, pageable);
		}else if (priority!=null) {
			tasks = tr.findByPriorityContainingIgnoreCase(priority, pageable);
		}else {
			tasks = tr.findAll(pageable);
		}
		return tasks.map(TaskMapper::toDto);
	}
	
	public TaskDto save(TaskDto dto) {
		Task task = TaskMapper.toTask(dto);
		tr.save(task);
		return TaskMapper.toDto(task);
	}
	
	public TaskDto update(Long id,TaskDto dto){
		if (dto.getDeadline().isBefore(LocalDate.now())) {
			throw new InvalidAtributeException("Deadline cannot be in the past");
		}
		Task task = tr.findById(id).orElseThrow(()->new TaskNotFoundException("Task not found!"));
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
		Task taskToBeDelete = tr.findById(id).orElseThrow(()-> new TaskNotFoundException("Task not found!"));
		tr.delete(taskToBeDelete);
	}
	
}
