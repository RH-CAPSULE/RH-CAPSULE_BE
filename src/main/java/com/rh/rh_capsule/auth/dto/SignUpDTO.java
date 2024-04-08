package com.rh.rh_capsule.auth.dto;


public record SignUpDTO(
        String userEmail,
        String password,
        String uuid
) {
}
