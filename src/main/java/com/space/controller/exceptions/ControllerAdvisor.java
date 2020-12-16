package com.space.controller.exceptions;

import com.space.controller.exceptions.InvalidArgException;
import com.space.controller.exceptions.ShipNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ShipNotFoundException.class})
    public ResponseEntity<Object> handleShipNotFoundException(ShipNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Ship not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidArgException.class})
    public ResponseEntity<Object> handleInvalidIdException(InvalidArgException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "provided id is invalid");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
