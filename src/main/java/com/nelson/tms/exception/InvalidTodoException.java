package com.nelson.tms.exception;

public class InvalidTodoException extends RuntimeException{
    public InvalidTodoException(String message) {
        super(message);
    }
}
