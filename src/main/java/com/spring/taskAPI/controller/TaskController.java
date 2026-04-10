package com.spring.taskAPI.controller;

import javax.naming.directory.InvalidAttributesException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	private final TaskService ts;
	public TaskController(TaskService ts) {
		super();
		this.ts = ts;
	}
	@GetMapping
	public ResponseEntity<Page<TaskDto>> list(Pageable pageable,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) Priority priority){
		return new ResponseEntity<>(ts.list(title, status, priority, pageable),HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<TaskDto> save(@RequestBody @Valid TaskDto dto){
		return new ResponseEntity<>(ts.save(dto),HttpStatus.CREATED);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		ts.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@PutMapping("/{id}")
	public ResponseEntity<TaskDto> update(@PathVariable long id, @RequestBody @Valid TaskDto dto) throws InvalidAttributesException{
		return new ResponseEntity<>(ts.update(id, dto),HttpStatus.OK);
	}
}
