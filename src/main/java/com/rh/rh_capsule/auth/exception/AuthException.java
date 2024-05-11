package com.rh.rh_capsule.auth.exception;

import com.rh.rh_capsule.exception.BaseException;
import lombok.Getter;

@Getter
public class AuthException extends BaseException {

    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode);
    }
}
