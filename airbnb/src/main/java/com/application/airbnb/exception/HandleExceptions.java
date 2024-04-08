package com.application.airbnb.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class HandleExceptions {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleInvalidArguments(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
        log.warn("One or more fields has the incorrect data.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponse> notFoundException(Exception exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error: ", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({DataBaseException.class})
    public ResponseEntity<ErrorResponse> databaseError(Exception exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error: ", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
