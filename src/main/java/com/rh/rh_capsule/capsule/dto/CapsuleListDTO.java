package com.rh.rh_capsule.capsule.dto;

import java.time.LocalDateTime;

public record CapsuleListDTO(
        Long capsuleId,
        String color,
        String title,
        String writer,
        Boolean isMine,
        LocalDateTime createdAt
) {
}
