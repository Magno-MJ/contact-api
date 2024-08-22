package com.study.contactapi.http.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.contactapi.http.exceptions.general.customexception.CustomHttpException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<Map<String, String>> handleCustomHttpExceptions(CustomHttpException exception) {
        Map<String, String> formattedMessage = new HashMap<>();

        formattedMessage.put("message", exception.getMessage());

        return new ResponseEntity<>(formattedMessage, exception.getHttpStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleJwtExceptions(UsernameNotFoundException exception) {
        Map<String, String> formattedMessage = new HashMap<>();

        formattedMessage.put("message", exception.getMessage());

        return new ResponseEntity<>(formattedMessage, HttpStatus.NOT_FOUND);
    }
}
