package com.spring.taskAPI.exception;

public class TokenWasExpiredException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TokenWasExpiredException(String message) {
		super(message);
	}
}
