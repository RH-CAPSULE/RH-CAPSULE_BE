package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.dto.SignUpDTO;
import com.rh.rh_capsule.auth.dto.TokenResponse;
import com.rh.rh_capsule.auth.dto.UserDTO;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.service.AuthService;
import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.auth.support.AuthenticationContext;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final AuthenticationContext authenticationContext;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> signUpProcess(@RequestBody SignUpDTO signUpDTO) {
        Long id = authService.signUp(signUpDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/auth/signin")
    public ResponseEntity<TokenResponse> signInProcess(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.signIn(userDTO));
    }

    @PostMapping("/api/signout")
    public ResponseEntity<?> signOut(@AuthUser Long userId, HttpServletRequest request) {
        AuthenticationExtractor.extractAccessToken(request).ifPresent(accessToken -> authService.signOut(userId, accessToken));
        return ResponseEntity.ok().body("로그이웃 되었습니다.");
    }
}