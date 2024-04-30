package com.rh.rh_capsule.auth.controller.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
