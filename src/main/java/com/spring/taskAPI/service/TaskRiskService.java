package com.spring.taskAPI.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spring.taskAPI.dto.TaskRiskAiDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.repository.TaskRepository;

@Service
public class TaskRiskService {
	private final TaskRepository tr;

	public TaskRiskService(TaskRepository tr) {
		this.tr = tr;
	}

	public Page<TaskRiskAiDto> getLateTasks(Pageable pageable) {
		String login = getLoggedUser();
		Page<Task> pendingTasks = tr.findByStatusAndUserLogin(Status.PENDING, login, pageable);
		return pendingTasks.map(task -> {
			long days = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());
			String risk = calculateRisk(task, days);
			return new TaskRiskAiDto(task.getId(), days, risk, task.getTitle());
		});
	}

	public String calculateRisk(Task task, long days) {
		int score = 0;
		if (task.getPriority() == Priority.HIGH)
			score += 5;
		if (days <= 1)
			score += 5;
		if (days <= 3)
			score += 3;
		if (score >= 8)
			return "CRITICAL";
		if (score >= 5)
			return "HIGH";
		return "NORMAL";
	}

	private String getLoggedUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}