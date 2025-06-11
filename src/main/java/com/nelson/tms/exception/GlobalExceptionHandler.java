package com.nelson.tms.exception;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTodoException.class)
    @RequestBody
    public ResponseEntity<String> handleInvalidTodo(InvalidTodoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @RequestBody
    public ResponseEntity<String> handleGeneralException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }

    @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @RequestBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Malformed JSON or invalid types in request.");
    }

    @RequestBody
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<HttpStatus> handleTodoNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<HttpStatus> handleInvalidPassword() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @RequestBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpStatus> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleExistingUsername() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}