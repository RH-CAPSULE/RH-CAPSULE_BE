package com.rh.rh_capsule.capsule.exception;

import lombok.Getter;

@Getter
public enum CapsuleErrorCode {
    CAPSULE_BOX_CREATE_FAILED(400, 3000, "캡슐함 생성에 실패했습니다."),
    USER_NOT_FOUND(404, 3001, "요청된 사용자를 찾을 수 없습니다."),
    CAPSULE_BOX_NOT_FOUND(404, 3002, "요청된 캡슐함을 찾을 수 없습니다."),
    CAPSULE_CREATE_FAILED(400, 3003, "캡슐 생성에 실패했습니다."),
    ACTIVE_CAPSULE_BOX_NOT_FOUND(404, 3004, "활성화된 캡슐함을 찾을 수 없습니다."),
    ACTIVE_CAPSULE_BOX_ALREADY_EXISTS(401, 3005, "이미 활성화된 캡슐함이 존재합니다."),
    NOT_ACTIVE_CAPSULE_BOX(401, 3006, "봉인 날짜가 지난 캡슐함에는 캡슐을 넣을 수 없습니다."),
    CAPSULE_BOX_DELETE_FAILED(400, 3007, "캡슐함 삭제에 실패했습니다."),
    CAPSULE_NOT_FOUND(404, 3008, "요청된 캡슐을 찾을 수 없습니다.");

    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    CapsuleErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
