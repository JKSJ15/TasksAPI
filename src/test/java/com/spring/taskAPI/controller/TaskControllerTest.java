package com.spring.taskAPI.controller;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.service.TaskService;
import com.spring.taskAPI.util.TaskUtilTest;


@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
	@InjectMocks
	TaskController tc;
	@Mock
	TaskService ts;
	PageImpl<TaskDto> list;
	Task task;
	TaskDto dto;
	
	@BeforeEach
	void setup() {
		task = TaskUtilTest.returnTask();
        dto = TaskUtilTest.returnTaskDto();
        dto.setId(1l);
	}
	
	@Test
	@DisplayName("list_returnListOfTasks_whenSucessful")
	void list_returnListOfTasks_whenSucessful() {
		list = new PageImpl<TaskDto>(List.of(dto));
		BDDMockito.when(ts.list(
		        ArgumentMatchers.anyString(),
		        ArgumentMatchers.any(Status.class),
		        ArgumentMatchers.any(Priority.class),
		        ArgumentMatchers.any(Pageable.class)
		)).thenReturn(list);
		
		TaskDto task = dto;
		Pageable pageable = Pageable.unpaged();
		ResponseEntity<Page<TaskDto>> find = this.tc.list(pageable, task.getTitle(), task.getStatus(), task.getPriority());
		
		Assertions.assertThat(find.getBody()).isNotNull();
		Assertions.assertThat(find.getBody().getContent()).isNotEmpty();
		Assertions.assertThat(find.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	@DisplayName("save_returnTaskDto_whenSucessful")
	void save_returnTaskDto_whenSucessful() {
		BDDMockito.when(ts.save(ArgumentMatchers.any(TaskDto.class))).thenReturn(dto);
		TaskDto task = dto;
		ResponseEntity<TaskDto> dto = tc.save(task);
		
		Assertions.assertThat(dto).isNotNull();
		Assertions.assertThat(dto.getBody()).usingRecursiveComparison().isEqualTo(task);
		Assertions.assertThat(dto.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	@Test
	@DisplayName("delete_deleteTask_whenSucessful")
	void delete_deleteTask_whenSucessful() {
		BDDMockito.doNothing().when(ts).delete(ArgumentMatchers.anyLong());
		
		Task taskToBeDeleted = task;
		taskToBeDeleted.setId(1l);
		tc.delete(taskToBeDeleted.getId());
		
		Mockito.verify(ts).delete(taskToBeDeleted.getId());
	}
	@Test
	@DisplayName("findById_returnTask_whenSucessful")
	void findById_returnTask_whenSucessful() {
		BDDMockito.when(ts.findById(ArgumentMatchers.anyLong())).thenReturn(dto);
		
		TaskDto task = dto;
		task.setId(1l);
		ResponseEntity<TaskDto> find = tc.findById(1l);
		
		Assertions.assertThat(find.getBody()).isNotNull();
		Assertions.assertThat(find.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
