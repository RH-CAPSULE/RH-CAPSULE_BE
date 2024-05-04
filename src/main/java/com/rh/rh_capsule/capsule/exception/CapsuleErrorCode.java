package com.rh.rh_capsule.capsule.exception;

import lombok.Getter;

@Getter
public enum CapsuleErrorCode {
    CAPSULE_BOX_CREATE_FAILED(400, 3000, "캡슐함 생성에 실패했습니다."),
    USER_NOT_FOUND(404, 3001, "요청된 사용자를 찾을 수 없습니다."),
    CAPSULE_BOX_NOT_FOUND(404, 3002, "요청된 캡슐함을 찾을 수 없습니다."),
    CAPSULE_CREATE_FAILED(400, 3003, "캡슐 생성에 실패했습니다.");

    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    CapsuleErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
