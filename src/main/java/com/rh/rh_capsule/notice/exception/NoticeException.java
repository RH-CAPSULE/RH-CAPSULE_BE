package com.rh.rh_capsule.notice.exception;

import com.rh.rh_capsule.exception.BaseException;

public class NoticeException extends BaseException {

    public NoticeException(NoticeErrorCode errorCode) {
        super(errorCode);
    }
}
