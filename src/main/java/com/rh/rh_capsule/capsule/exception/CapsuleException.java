package com.rh.rh_capsule.capsule.exception;

import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import lombok.Getter;
@Getter
public class CapsuleException extends RuntimeException {
    private final CapsuleErrorCode capsuleErrorCode;

    public CapsuleException(CapsuleErrorCode capsuleErrorCode) {
        super(capsuleErrorCode.getMessage());
        this.capsuleErrorCode = capsuleErrorCode;
    }
}

