package com.rh.rh_capsule.auth.controller.dto;

public record VerifyMailDTO (
        String userEmail,
        String code
) {
}
