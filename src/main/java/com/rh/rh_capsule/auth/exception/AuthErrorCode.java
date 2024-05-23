package com.rh.rh_capsule.auth.exception;

import com.rh.rh_capsule.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthErrorCode implements ErrorCode {
    UNAUTHORIZED(401, 1001,"인증되지 않은 사용자입니다."),
    TOKEN_EMPTY(401, 1002,"토큰이 헤더에 없습니다."),
    ALREADY_SIGN_OUT(401, 1003, "이미 로그아웃 된 사용자입니다."),
    EMAIL_ALREADY_EXISTS(409,1004, "이미 사용중인 이메일입니다."),
    EXPIRED_TOKEN(401, 1005,"토큰이 만료되었습니다."),
    INVALID_VERIFICATION_CODE(401, 1006, "인증 코드가 유효하지 않습니다."),
    INVALID_SIGNATURE(401, 1007,"토큰의 서명이 유효하지 않습니다."),
    MALFORMED_TOKEN(401, 1008, "토큰 구조가 잘못되었습니다."),
    UNSUPPORTED_TOKEN(401, 1009,"토큰 유형이 지원되지 않습니다."),
    INVALID_TOKEN(401, 1010, "토큰이 유효하지 않거나 누락되었습니다."),
    INVALID_TOKEN_FORMAT(401, 1011,"토큰의 형식이 유효하지 않습니다."),
    SECURITY_ERROR(401, 1012, "토큰 처리 중 보안 오류가 발생했습니다."),
    JWT_ERROR(401, 1013, "토큰 관련 오류가 발생했습니다."),
    INVALID_VERIFICATION(401, 1014, "이메일 인증이 완료되지 않았습니다."),
    EMAIL_NOT_FOUND(404, 1015, "이메일이 존재하지 않습니다."),
    USER_NOT_FOUND(404, 1016, "사용자를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(400, 1017, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_INPUT(400, 1018, "입력값이 유효하지 않습니다."),
    OAUTH_UNEXPECTED_ERROR(400, 2000, "OAuth 요청 중 예기치 않은 오류가 발생했습니다."),
    HTTP_REQUEST_FAILED(400, 2001, "HTTP 요청이 실패했습니다."),
    SERVICE_UNAVAILABLE(503,2002, "서비스를 이용할 수 없습니다."),
    DELETED_USER(403, 1019, "탈퇴한 사용자입니다."),;

    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    AuthErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
