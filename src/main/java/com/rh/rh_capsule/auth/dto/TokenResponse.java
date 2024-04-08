package com.rh.rh_capsule.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
