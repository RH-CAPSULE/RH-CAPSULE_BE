package com.rh.rh_capsule.capsule.exception;

import com.rh.rh_capsule.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum CapsuleErrorCode implements ErrorCode {
    CAPSULE_BOX_CREATE_FAILED(400, 3000, "캡슐함 생성에 실패했습니다."),
    USER_NOT_FOUND(404, 3001, "요청된 사용자를 찾을 수 없습니다."),
    CAPSULE_BOX_NOT_FOUND(404, 3002, "요청된 캡슐함을 찾을 수 없습니다."),
    CAPSULE_CREATE_FAILED(400, 3003, "캡슐 생성에 실패했습니다."),
    ACTIVE_CAPSULE_BOX_NOT_FOUND(404, 3004, "활성화된 캡슐함을 찾을 수 없습니다."),
    ACTIVE_CAPSULE_BOX_ALREADY_EXISTS(409, 3005, "이미 활성화된 캡슐함이 존재합니다."),
    NOT_ACTIVE_CAPSULE_BOX(403, 3006, "봉인 날짜가 지난 캡슐함에는 캡슐을 넣을 수 없습니다."),
    CAPSULE_BOX_DELETE_FAILED(400, 3007, "캡슐함 삭제에 실패했습니다."),
    CAPSULE_NOT_FOUND(404, 3008, "요청된 캡슐을 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAILED(400, 3009, "이미지 업로드에 실패했습니다."),
    AUDIO_UPLOAD_FAILED(400, 3010, "오디오 업로드에 실패했습니다."),
    EXISTING_OWN_CAPSULE(409, 3011, "이미 내 캡슐이 존재합니다."),
    INVALID_CAPSULE_BOX_USER(403, 3012, "캡슐함의 사용자와 요청한 사용자가 일치하지 않습니다."),
    NOT_OPENED_CAPSULE_BOX(403, 3013, "아직 봉인된 캡슐함입니다."),;

    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    CapsuleErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
