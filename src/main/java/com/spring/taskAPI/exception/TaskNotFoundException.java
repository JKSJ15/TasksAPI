package com.spring.taskAPI.exception;

public class TaskNotFoundException extends RuntimeException{
	public TaskNotFoundException(String message) {
		super(message);
	}
}
