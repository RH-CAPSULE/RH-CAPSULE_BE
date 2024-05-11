package com.rh.rh_capsule.exception;

public interface ErrorCode {
    int getStatusCode();
    int getExceptionCode();
    String getMessage();
}
