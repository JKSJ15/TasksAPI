package com.spring.taskAPI.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskAPI.dto.TaskRiskAiDto;
import com.spring.taskAPI.service.TaskRiskService;

@RestController
@RequestMapping("/tasks")
public class TaskRiskController {
	private final TaskRiskService tr;

	public TaskRiskController(TaskRiskService tr) {
		super();
		this.tr = tr;
	}

	@GetMapping("/risk")
	public ResponseEntity<Page<TaskRiskAiDto>> getRiskTasks(Pageable pageable) {
		return ResponseEntity.ok(tr.getLateTasks(pageable));
	}
}
