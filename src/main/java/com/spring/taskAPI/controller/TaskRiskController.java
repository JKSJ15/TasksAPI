package com.spring.taskAPI.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskAPI.dto.TaskRiskAiDto;
import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.service.TaskRiskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Risk Tasks")
public class TaskRiskController {
	private final TaskRiskService tr;

	public TaskRiskController(TaskRiskService tr) {
		super();
		this.tr = tr;
	}

	@Operation(summary = "Get user risk tasks", description = "Retrieve all tasks of the authenticated user that are close to the deadline")

	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tasks successfully retrieved", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TaskRiskAiDto.class)))),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@GetMapping("/risk")
	public ResponseEntity<Page<TaskRiskAiDto>> getRiskTasks(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(tr.getLateTasks(pageable));
	}
}
