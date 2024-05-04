package com.rh.rh_capsule.capsule.dto;

public record CapsuleCreateDTO(
        String color,
        String title,
        String content,
        String writer,
        Long capsuleBoxId,
        Boolean isMine
) {
}
