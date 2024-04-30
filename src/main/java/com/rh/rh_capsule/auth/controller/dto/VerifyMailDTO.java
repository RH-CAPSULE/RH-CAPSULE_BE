package com.rh.rh_capsule.auth.dto;

public record VerifyMailDTO (
        String userEmail,
        String code
) {
}
