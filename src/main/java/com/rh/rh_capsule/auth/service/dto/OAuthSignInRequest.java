package com.rh.rh_capsule.auth.service.dto;

public record OAuthSignInRequest(
        String redirectUri,
        String code
){
}
