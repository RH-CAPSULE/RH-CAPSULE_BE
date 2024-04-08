package com.rh.rh_capsule.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleCustomException(AuthException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", errorCode.getStatusCode());
        body.put("errorCode", errorCode.getExceptionCode());
        body.put("message", errorCode.getMessage());

        return new ResponseEntity<>(body, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}

