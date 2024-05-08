package com.rh.rh_capsule.auth.controller.dto;

public record SignUpDTO(
        String userEmail,
        String password,
        String username
) {
}
