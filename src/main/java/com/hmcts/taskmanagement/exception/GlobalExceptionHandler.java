package com.hmcts.taskmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex)
    {
        String errors=ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error->error.getField() +":"+error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex)
    {
        return  new ResponseEntity<>("Internal Server Error: " +ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
