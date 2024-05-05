package com.rh.rh_capsule.capsule.dto;

import java.time.LocalDateTime;

public record CapsuleDTO(
        Long id,
        String color,
        String title,
        String content,
        String writer,
        String imageUrl,
        String audioUrl,
        Boolean isMine,
        LocalDateTime createdAt
) {
}
