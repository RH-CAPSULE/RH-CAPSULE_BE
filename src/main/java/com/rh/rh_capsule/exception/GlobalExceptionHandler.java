package com.rh.rh_capsule.exception;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
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
        AuthErrorCode authErrorCode = ex.getAuthErrorCode();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", authErrorCode.getStatusCode());
        body.put("errorCode", authErrorCode.getExceptionCode());
        body.put("message", authErrorCode.getMessage());

        return new ResponseEntity<>(body, HttpStatus.valueOf(authErrorCode.getStatusCode()));
    }
}

