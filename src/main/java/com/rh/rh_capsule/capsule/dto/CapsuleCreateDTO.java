package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleTheme;

public record CapsuleCreateDTO(
        Long capsuleBoxId,
        String color,
        CapsuleTheme theme,
        String title,
        String content,
        String writer
) {
}
