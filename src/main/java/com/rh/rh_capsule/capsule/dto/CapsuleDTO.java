package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleTheme;

import java.time.LocalDateTime;

public record CapsuleDTO(
        Long capsuleId,
        String color,
        CapsuleTheme theme,
        String title,
        String content,
        String writer,
        String imageUrl,
        String audioUrl,
        Boolean isMine,
        LocalDateTime createdAt
) {
}
