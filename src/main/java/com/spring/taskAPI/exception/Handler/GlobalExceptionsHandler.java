package com.spring.taskAPI.exception.Handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.InvalidRefreshTokenException;
import com.spring.taskAPI.exception.TaskNotFoundException;
import com.spring.taskAPI.exception.TokenWasExpiredException;
import com.spring.taskAPI.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionsHandler {
	@ExceptionHandler(value = InvalidAtributeException.class)
	public ResponseEntity<BodyExceptions> useInvalidAtributeException(InvalidAtributeException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(value = TaskNotFoundException.class)
	public ResponseEntity<BodyExceptions> useTaskNotFoundException(TaskNotFoundException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.NOT_FOUND).withStatus(HttpStatus.NOT_FOUND.value()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(value = InvalidRefreshTokenException.class)
	public ResponseEntity<BodyExceptions> useInvalidRefreshTokenException(InvalidRefreshTokenException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED.value()).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(value = TokenWasExpiredException.class)
	public ResponseEntity<BodyExceptions> useTokenWasExpiredException(TokenWasExpiredException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED.value()).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<BodyExceptions> useUserNotFoundException(UserNotFoundException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.NOT_FOUND).withStatus(HttpStatus.NOT_FOUND.value()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
}
