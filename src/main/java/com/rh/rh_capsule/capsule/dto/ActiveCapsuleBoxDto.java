package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleBoxTheme;

import java.time.LocalDateTime;
import java.util.List;

public record ActiveCapsuleBoxDto(
        Long capsuleBoxId,
        CapsuleBoxTheme theme,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        List<String> capsules
) {
}
