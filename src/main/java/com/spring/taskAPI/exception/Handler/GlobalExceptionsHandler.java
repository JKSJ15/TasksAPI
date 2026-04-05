package com.spring.taskAPI.exception.Handler;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.TaskNotFoundException;

@RestControllerAdvice
public class GlobalExceptionsHandler {
	@ExceptionHandler
	public ResponseEntity<BodyExceptions> useInvalidAtributeException(InvalidAtributeException e){
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
		.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	@ExceptionHandler
	public ResponseEntity<BodyExceptions> useTaskNotFoundException(TaskNotFoundException e){
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
		.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
}
