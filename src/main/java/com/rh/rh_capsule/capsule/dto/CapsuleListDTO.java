package com.rh.rh_capsule.capsule.dto;

import java.time.LocalDateTime;

public record CapsuleListDTO(
        Long id,
        String color,
        String writer,
        Boolean isMine,
        LocalDateTime createdAt
) {
}
