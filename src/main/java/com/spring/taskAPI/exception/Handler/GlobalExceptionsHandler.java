package com.spring.taskAPI.exception.Handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.spring.taskAPI.exception.BodyExceptions;
import com.spring.taskAPI.exception.InvalidAccessTokenException;
import com.spring.taskAPI.exception.InvalidAtributeException;
import com.spring.taskAPI.exception.InvalidRefreshTokenException;
import com.spring.taskAPI.exception.TaskNotFoundException;
import com.spring.taskAPI.exception.UserAlreadyExistsExeption;
import com.spring.taskAPI.exception.UserNotFoundException;

import jakarta.validation.ConstraintViolationException;

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

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<BodyExceptions> useUserNotFoundException(UserNotFoundException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.NOT_FOUND).withStatus(HttpStatus.NOT_FOUND.value()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(value = UserAlreadyExistsExeption.class)
	public ResponseEntity<BodyExceptions> useUserAlreadyExistsExeption(UserAlreadyExistsExeption e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.CONFLICT).withStatus(HttpStatus.CONFLICT.value()).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
	}

	@ExceptionHandler(value = InvalidAccessTokenException.class)
	public ResponseEntity<BodyExceptions> useInvalidAccessTokenException(InvalidAccessTokenException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED.value()).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<BodyExceptions> useMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<BodyExceptions> useConstraintViolationException(ConstraintViolationException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getConstraintViolations().iterator().next().getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<BodyExceptions> useMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage("Invalid method argument type").withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<BodyExceptions> useHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage("Invalid request body").withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.BAD_REQUEST).withStatus(HttpStatus.BAD_REQUEST.value()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<BodyExceptions> useBadCredentialsException(BadCredentialsException e) {
		BodyExceptions body = BodyExceptions.builder().withMessage(e.getMessage()).withTimestamp(LocalDateTime.now())
				.withErro(HttpStatus.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED.value()).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
}
