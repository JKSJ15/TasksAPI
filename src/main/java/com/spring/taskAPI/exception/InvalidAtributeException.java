package com.spring.taskAPI.exception;

public class InvalidAtributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidAtributeException(String message) {
		super(message);
	}
}
