package com.spring.taskAPI.service;

import java.time.LocalDate;
import java.util.Optional;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.TaskNotFoundException;
import com.spring.taskAPI.repository.TaskRepository;
import com.spring.taskAPI.repository.UserRepository;
import com.spring.taskAPI.util.TaskUtilTest;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@InjectMocks
	TaskService ts;
	@Mock
	TaskRepository tr;
	@Mock
	UserRepository ur;
	User user;
	Task task;
	TaskDto dto;
	
	@BeforeEach
	void setup() {
		user = new User();
	    user.setId(1L);
	    user.setLogin("jakson");

	    task = TaskUtilTest.returnTask();
	    task.setId(2L);
	    task.setUser(user);

	    dto = TaskUtilTest.returnTaskDto();
	    dto.setId(10L);

	    UsernamePasswordAuthenticationToken auth =
	            new UsernamePasswordAuthenticationToken(
	                    "jakson",
	                    null,
	                    null
	            );
	    SecurityContext context = Mockito.mock(SecurityContext.class);
	    Mockito.when(context.getAuthentication()).thenReturn(auth);
	    SecurityContextHolder.setContext(context);
	}
	
	@Test
	@DisplayName("save_returnsTaskDtoObject_wheSucessfull")
	void save_returnsTaskDtoObject_wheSucessfull() {
		Mockito.when(ur.findByLogin("jakson"))
        .thenReturn(Optional.of(user));
		Mockito.when(tr.save(ArgumentMatchers.any(Task.class))).thenReturn(task);
		
		TaskDto taskSaved = ts.save(dto);
		
		Assertions.assertThat(taskSaved).isNotNull();
		Assertions.assertThat(taskSaved.getId()).isNotNull();
	}
	@Test
	@DisplayName("save_throwInvalidAtributeException_whenDeadLineIsBeforeDateNow")
	void save_throwInvalidAtributeException_whenDeadLineIsBeforeDateNow() {
		Mockito.when(ur.findByLogin("jakson"))
	       .thenReturn(Optional.of(user));
		Mockito.when(tr.save(ArgumentMatchers.any(Task.class))).thenReturn(task);
		
		TaskDto taskToBeSaved = ts.save(dto);
		taskToBeSaved.setDeadline(LocalDate.of(1000, 10, 10));
		
		Assertions.assertThatThrownBy(()->ts.save(taskToBeSaved)).isInstanceOf(InvalidAtributeException.class);
	}
	@Test
	@DisplayName("update_returnsTaskDtoObject_whenSuccessful")
	void update_returnsTaskDtoObject_whenSuccessful() {
	    dto.setId(2L);
	    dto.setTitle("Macarena");

	    Mockito.when(
	            tr.findByIdAndUserLogin(2L, "jakson")
	    ).thenReturn(Optional.of(task));
	    Mockito.when(tr.save(ArgumentMatchers.any(Task.class)))
	            .thenReturn(task);
	    TaskDto updated = ts.update(2L, dto);

	    Assertions.assertThat(updated).isNotNull();
	    Assertions.assertThat(updated.getTitle())
	            .isEqualTo("Macarena");
	}
	@Test
	@DisplayName("findById_throwTaskNotFoundException_whenNotFound")
	void findById_throwTaskNotFoundException_whenNotFound() {
		Mockito.when(
			    tr.findByIdAndUserLogin(
			        ArgumentMatchers.anyLong(),
			        ArgumentMatchers.anyString()
			    )
			).thenReturn(Optional.empty());
		Assertions.assertThatThrownBy(()->ts.findById(100000l)).isInstanceOf(TaskNotFoundException.class);
	}
	@Test
	@DisplayName("findById_returnTaskDtoObject_whenSucessful")
	void findById_returnTaskDtoObject_whenSucessful() {
		 Mockito.when(
		            tr.findByIdAndUserLogin(
		                    ArgumentMatchers.anyLong(),
		                    ArgumentMatchers.anyString()
		            )
		    ).thenReturn(Optional.of(task));
		
		TaskDto find = ts.findById(2);
		
		Assertions.assertThat(find).isNotNull();
		Assertions.assertThat(find).usingRecursiveComparison().isEqualTo(task);
	}
	@Test
	@DisplayName("delete_throwTaskNotFoundException_whenNotFound")
	void delete_throwTaskNotFoundException_whenNotFound() {
		Assertions.assertThatThrownBy(()->ts.delete(100000l)).isInstanceOf(TaskNotFoundException.class);
	}
	@Test
	@DisplayName("delete_throwTaskNotFoundException_whenNotFound")
	void delete_removeTask_whenSucessful() {
		BDDMockito.when(
		        tr.findByIdAndUserLogin(
		                ArgumentMatchers.anyLong(),
		                ArgumentMatchers.anyString()
		        )
		).thenReturn(Optional.of(task));
		BDDMockito.doNothing().when(tr).delete(task);

		ts.delete(1L);
	}
}
