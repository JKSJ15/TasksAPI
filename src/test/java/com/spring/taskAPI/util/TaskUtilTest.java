package com.spring.taskAPI.util;

import java.time.LocalDate;

import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.mapper.TaskMapper;

public class TaskUtilTest {
	public static Task returnTask() {
		return Task.builder().withCreatedAt(LocalDate.now())
				.withDateConcl(null)
				.withDeadline(LocalDate.now().plusDays(2))
				.withDescription("Walk with the Dog")
				.withPriority(Priority.MEDIUM)
				.withStatus(Status.PENDING)
				.withTitle("Walk With Dog")
				.build();
	}
	public static TaskDto returnTaskDto() {
		Task task = Task.builder().withCreatedAt(LocalDate.now())
				.withDateConcl(null)
				.withDeadline(LocalDate.now().plusDays(2))
				.withDescription("Walk with the Dog")
				.withPriority(Priority.MEDIUM)
				.withStatus(Status.PENDING)
				.withTitle("Walk With Dog")
				.build();
		return TaskMapper.toDto(task);
	}
	
}
