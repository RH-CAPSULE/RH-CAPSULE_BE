package com.rh.rh_capsule.inquiry.exception;

import com.rh.rh_capsule.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum InquiryErrorCode implements ErrorCode {
    ;
    int statusCode;
    int ExceptionCode;
    String message;
}
