package com.spring.taskAPI.mapper;

import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Task;

public class TaskMapper {
	public static TaskDto toDto(Task task) {
		return TaskDto.builder()
				.withId(task.getId())
				.withCreatedAt(task.getCreatedAt())
				.withDateConcl(task.getDateConcl())
				.withDeadline(task.getDeadline())
				.withDescription(task.getDescription())
				.withPriority(task.getPriority())
				.withStatus(task.getStatus())
				.withTitle(task.getTitle()).build();
	}
	public static Task toTask(TaskDto dto) {
		return Task.builder()
				.withId(dto.getId())
				.withCreatedAt(dto.getCreatedAt())
				.withDateConcl(dto.getDateConcl())
				.withDeadline(dto.getDeadline())
				.withDescription(dto.getDescription())
				.withPriority(dto.getPriority())
				.withStatus(dto.getStatus())
				.withTitle(dto.getTitle()).build();
	}
}
