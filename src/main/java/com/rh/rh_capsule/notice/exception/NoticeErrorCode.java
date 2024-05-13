package com.rh.rh_capsule.notice.exception;

import com.rh.rh_capsule.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum NoticeErrorCode implements ErrorCode {
    NOTICE_NOT_FOUND(404, 1001, "공지사항을 찾을 수 없습니다."),
    ;

    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    NoticeErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
