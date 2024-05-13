package com.rh.rh_capsule.notice.controller.dto;

import java.time.LocalDateTime;

public record NoticeDTO(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt
) {
}
