package com.rh.rh_capsule.capsule.dto;

public record CapsuleCreateDTO(
        Long capsuleBoxId,
        String color,
        String title,
        String content,
        String writer
) {
}
