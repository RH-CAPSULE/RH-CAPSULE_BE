package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleTheme;

import java.time.LocalDateTime;

public record CapsuleListDTO(
        Long capsuleId,
        String color,
        CapsuleTheme theme,
        String title,
        String writer,
        Boolean isMine,
        LocalDateTime createdAt
) {
}
