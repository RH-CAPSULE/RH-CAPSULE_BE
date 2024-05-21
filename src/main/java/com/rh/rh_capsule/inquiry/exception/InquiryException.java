package com.rh.rh_capsule.inquiry.exception;

import com.rh.rh_capsule.exception.BaseException;
import com.rh.rh_capsule.exception.ErrorCode;

public class InquiryException extends BaseException {
    public InquiryException(InquiryErrorCode errorCode) {
        super(errorCode);
    }
}
