package com.spring.taskAPI.exception;

public class UserAlreadyExistsExeption extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsExeption(String message) {
        super(message);
    }   

}
