package com.rh.rh_capsule.capsule.dto;

import com.rh.rh_capsule.capsule.domain.CapsuleBoxTheme;

import java.time.LocalDateTime;
import java.util.List;

public record GuestCapsuleBoxDTO(
        Long capsuleBoxId,
        String userName,
        CapsuleBoxTheme theme,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        Boolean hasMyCapsule,
        List<String> capsules)
{
}

