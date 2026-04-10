package com.spring.taskAPI.util;

import java.time.LocalDate;

import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;

public class TaskUtilTest {
	public static Task returnTask() {
		return Task.builder().withCreatedAt(LocalDate.now())
				.withDateConcl(null)
				.withDeadline(LocalDate.now().plusDays(2))
				.withDescription("Walk with the Dog")
				.withId(134L)
				.withPriority(Priority.MEDIUM)
				.withStatus(Status.PENDING)
				.withTitle("Walk With Dog")
				.build();
	}
	
}
