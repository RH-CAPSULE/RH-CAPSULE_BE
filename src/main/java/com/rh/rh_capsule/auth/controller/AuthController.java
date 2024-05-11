package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.controller.dto.*;
import com.rh.rh_capsule.auth.controller.dto.UserDTO;
import com.rh.rh_capsule.auth.service.AuthService;
import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.signIn(userDTO));
    }

    @PostMapping("/api/auth/sign-out")
    public ResponseEntity<?> signOut(@AuthUser Long userId, HttpServletRequest request) {
        AuthenticationExtractor.extractAccessToken(request).ifPresent(accessToken -> authService.signOut(userId, accessToken));
        return ResponseEntity.ok().body("로그이웃 되었습니다.");
    }
    @PostMapping("/api/auth/reset-pw")
    public ResponseEntity<String> resetPassword(@RequestBody UserDTO userDTO) {
        authService.resetPassword(userDTO);
        return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
    }

    @PostMapping("/api/auth/token-reissue")
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody TokenReissueDTO tokenReissueDTO,
                                                      @AuthUser Long userId) {
        return ResponseEntity.ok(authService.reissueToken(userId, tokenReissueDTO));
    }
}