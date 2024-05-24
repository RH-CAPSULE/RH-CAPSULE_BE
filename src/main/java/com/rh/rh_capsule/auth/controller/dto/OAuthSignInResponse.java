package com.rh.rh_capsule.auth.controller.dto;

public record OAuthSignInResponse(
        String accessToken,
        String refreshToken,
        boolean isFirstSignIn
) {
}
