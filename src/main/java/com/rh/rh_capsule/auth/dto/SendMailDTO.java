package com.rh.rh_capsule.auth.dto;

public record SendMailDTO(
        String userEmail,
        VerificationPurpose purpose
) {
}
