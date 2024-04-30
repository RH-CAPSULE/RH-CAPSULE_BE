package com.rh.rh_capsule.auth.controller.dto;

public record SendMailDTO(
        String userEmail,
        VerificationPurpose purpose
) {
}
