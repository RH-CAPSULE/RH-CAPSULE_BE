package com.rh.rh_capsule.capsule.exception;

import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.exception.BaseException;
import lombok.Getter;
@Getter
public class CapsuleException extends BaseException {

    public CapsuleException(CapsuleErrorCode capsuleErrorCode) {
        super(capsuleErrorCode);
    }
}

