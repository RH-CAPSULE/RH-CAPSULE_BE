package com.rh.rh_capsule.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        int exceptionCode,
        String message,
        LocalDateTime timestamp
) {
}
