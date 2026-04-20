package com.spring.taskAPI.repository;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.spring.taskAPI.entity.Priority;
import com.spring.taskAPI.entity.Status;
import com.spring.taskAPI.entity.Task;
import com.spring.taskAPI.util.TaskUtilTest;

@DataJpaTest
public class TaskRepositoryTest {
	@Autowired
	TaskRepository rep;
	
	@Test
	@DisplayName("Save Persister a task When Sucessful")
	void Save_PersisterTask_WhenSucessful() {
		Task task = TaskUtilTest.returnTask();
		Task taskSaved = rep.save(task);
		
		Assertions.assertThat(taskSaved).isNotNull();
		Assertions.assertThat(taskSaved.getId()).isNotNull();
		Assertions.assertThat(taskSaved.equals(task));
	}
	
	@Test
	@DisplayName("Save updates task When Sucessful")
	void Save_UpdatesTask_WhenSucessful() {
		Task task = TaskUtilTest.returnTask();
		Task taskSaved = rep.save(task);
		taskSaved.setPriority(Priority.HIGH);
		Task taskUpdated = rep.save(taskSaved);
		
		Assertions.assertThat(taskUpdated).isNotNull();
		Assertions.assertThat(taskUpdated.getId()).isNotNull();
		Assertions.assertThat(taskUpdated.getPriority()).isEqualTo(Priority.HIGH);
	}
	
	@Test
	@DisplayName("Delete remove task When Sucessful")
	void Delete_RemoveTask_WhenSucessful() {
		Task task = TaskUtilTest.returnTask();
		Task taskSaved = rep.save(task);
		rep.delete(taskSaved);

		
		Optional<Task> optional = rep.findById(task.getId());
		
		Assertions.assertThat(optional).isEmpty();
	}
	
	@Test
	@DisplayName("FindById return a task When Sucessful")
	void FindById_returnTask_WhenSucessful() {
		Task task = TaskUtilTest.returnTask();
		rep.save(task);
		Optional<Task> optional = rep.findById(task.getId());
		
		Assertions.assertThat(optional).isNotNull().isNotEmpty();
		Assertions.assertThat(optional.get()).isEqualTo(task);
	}
	@Test
	@DisplayName("FindById return empty When Not Find")
	void FindById_returnEmpty_WhenNotFind() {
		Optional<Task> optional = rep.findById(9999L);
		
		Assertions.assertThat(optional).isEmpty();
	}
	
	@Test
	@DisplayName("findByTitleContainingIgnoreCase return a task When Sucessful")
	void findByTitleContainingIgnoreCase_returnTask_WhenSucessful() {
		Pageable pageable = PageRequest.of(0, 10);
		Task task = TaskUtilTest.returnTask();
		rep.save(task);
		Page<Task> optional = rep.findByTitleContainingIgnoreCaseAndUserLogin("Walk With Dog","alberto", pageable);
		
		Assertions.assertThat(optional).isNotNull().isNotEmpty();
	}
	@Test
	@DisplayName("findByTitleContainingIgnoreCase return empty When Not Found")
	void findByTitleContainingIgnoreCase_returnEmpty_WhenNotFound() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Task> optional = rep.findByTitleContainingIgnoreCaseAndUserLogin("OS99DASUST","alberto", pageable);
		
		Assertions.assertThat(optional).isEmpty();
	}
	//-----------------------------------------------------------
	@Test
	@DisplayName("findByStatus return a task When Sucessful")
	void findByStatus_returnTask_WhenSucessful() {
		Pageable pageable = PageRequest.of(0, 10);
		Task task = TaskUtilTest.returnTask();
		rep.save(task);
		Page<Task> optional = rep.findByStatusAndUserLogin(Status.PENDING,"alberto", pageable);
		
		Assertions.assertThat(optional).isNotNull().isNotEmpty();
	}
	@Test
	@DisplayName("findByStatus return empty When Not Find")
	void findByStatus_returnEmpty_WhenNotFind() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Task> optional = rep.findByStatusAndUserLogin(Status.COMPLETED,"alberto", pageable);
		
		Assertions.assertThat(optional).isEmpty();
	}
	//--------------------------------------------------------------
	@Test
	@DisplayName("findByPriority return a task When Sucessful")
	void findByPriority_returnTask_WhenSucessful() {
		Pageable pageable = PageRequest.of(0, 10);
		Task task = TaskUtilTest.returnTask();
		rep.save(task);
		Page<Task> optional = rep.findByPriorityAndUserLogin(Priority.MEDIUM,"karlao", pageable);
		
		Assertions.assertThat(optional).isNotNull().isNotEmpty();
	}
	@Test
	@DisplayName("findByPriority return empty When Not Find")
	void findByPriority_returnEmpty_WhenNotFind() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Task> optional = rep.findByPriorityAndUserLogin(Priority.LOW,"karlao", pageable);
		
		Assertions.assertThat(optional).isEmpty();
	}
}
