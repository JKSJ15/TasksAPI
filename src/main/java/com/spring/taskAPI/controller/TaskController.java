package com.spring.taskAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Task")
@RestController
@RequestMapping("/tasks")
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	// GET ALL
	@Operation(summary = "Get user tasks", description = "Retrieve all tasks of the authenticated user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tasks successfully retrieved", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@GetMapping
	public ResponseEntity<Page<TaskDto>> list(@ParameterObject Pageable pageable,
			@Parameter(description = "Filter by title") @RequestParam(required = false) String title,
			@Parameter(description = "Filter by status") @RequestParam(required = false) Status status,
			@Parameter(description = "Filter by priority") @RequestParam(required = false) Priority priority) {

		logger.info("GET /tasks filters -> title: {}, status: {}, priority: {}", title, status, priority);
		return new ResponseEntity<>(taskService.list(title, status, priority, pageable), HttpStatus.OK);

	}

	// GETBYID
	@Operation(summary = "Get user task by ID", description = "Retrieve a specific task by its ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Task successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@GetMapping("/{id}")
	public ResponseEntity<TaskDto> findById(@Parameter(description = "ID of the task") @PathVariable long id) {

		logger.info("GET /tasks/{} request received", id);
		return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);

	}

	@Operation(summary = "Create new task", description = "Save a new task for the user")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Task successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PostMapping
	public ResponseEntity<TaskDto> save(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task data to be saved") @RequestBody @Valid TaskDto dto) {

		logger.info("POST /tasks create request received");
		return new ResponseEntity<>(taskService.save(dto), HttpStatus.CREATED);

	}

	@Operation(summary = "Delete task by ID", description = "Remove a task by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@Parameter(description = "ID of the task") @PathVariable long id) {

		logger.info("DELETE /tasks/{} request received", id);
		taskService.delete(id);
		logger.info("Task {} deleted", id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@Operation(summary = "Update task by ID", description = "Update an existing task by its ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Task successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized or missing token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))),
			@ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BodyExceptions.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<TaskDto> update(@Parameter(description = "ID of the task") @PathVariable long id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task data to be updated") @RequestBody @Valid TaskDto dto) {

		logger.info("PUT /tasks/{} request received", id);
		TaskDto updated = taskService.update(id, dto);
		logger.info("Task {} updated successfully", id);

		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
}
