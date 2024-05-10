package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleBoxTheme;

import java.time.LocalDateTime;

public record HistoryCapsuleBoxes(
        Long capsuleBoxId,
        CapsuleBoxTheme theme,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        LocalDateTime createdAt
) {
}
