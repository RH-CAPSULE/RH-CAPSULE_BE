package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.controller.dto.SignInUriResponse;
import com.rh.rh_capsule.auth.controller.dto.TokenResponse;
import com.rh.rh_capsule.auth.domain.OAuthUser;
import com.rh.rh_capsule.auth.infrastructure.RestTemplateOAuthRequester;
import com.rh.rh_capsule.auth.service.OAuthService;
import com.rh.rh_capsule.auth.service.dto.OAuthSignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    private final RestTemplateOAuthRequester restTemplateOAuthRequester;

    @GetMapping("/oauth/{provider}/sign-in-uri")
    public ResponseEntity<SignInUriResponse> signInUri(
            @PathVariable String provider,
            @RequestParam("redirect-uri") String redirectUri
    ) {
        String signInUri = oAuthService.signInUri(redirectUri, provider);
        return ResponseEntity.ok(new SignInUriResponse(signInUri));
    }

    @PostMapping("/oauth/{provider}/sign-in")
    public ResponseEntity signIn(@RequestBody OAuthSignInRequest request, @PathVariable String provider){
        OAuthUser oAuthUser = restTemplateOAuthRequester.signIn(request, provider);
        TokenResponse tokens = oAuthService.signIn(oAuthUser);
        return ResponseEntity.ok(tokens);

    }
}
