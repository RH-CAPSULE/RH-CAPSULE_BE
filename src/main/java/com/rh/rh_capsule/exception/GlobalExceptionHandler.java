package com.rh.rh_capsule.exception;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
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

    @ExceptionHandler(CapsuleException.class)
    public ResponseEntity<Object> handleCustomException(CapsuleException ex) {
        CapsuleErrorCode capsuleErrorCode = ex.getCapsuleErrorCode();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", capsuleErrorCode.getStatusCode());
        body.put("errorCode", capsuleErrorCode.getExceptionCode());
        body.put("message", capsuleErrorCode.getMessage());

        return new ResponseEntity<>(body, HttpStatus.valueOf(capsuleErrorCode.getStatusCode()));
    }
}

