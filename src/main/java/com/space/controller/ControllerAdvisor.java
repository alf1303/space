package com.space.controller;

import com.space.controller.exceptions.InvalidIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerAdvisor  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleShipNotFoundException(NoSuchElementException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Ship not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidIdException.class})
    public ResponseEntity<Object> handleInvalidIdException(InvalidIdException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "provided id is invalid");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
