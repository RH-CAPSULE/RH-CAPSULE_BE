package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.dto.SignUpDTO;
import com.rh.rh_capsule.auth.dto.TokenResponse;
import com.rh.rh_capsule.auth.dto.UserDTO;
import com.rh.rh_capsule.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> signUpProcess(SignUpDTO signUpDTO) {
        Long id = authService.SignUpProcess(signUpDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/auth/signin")
    public ResponseEntity<TokenResponse> signInProcess(UserDTO userDTO) {
        return ResponseEntity.ok(authService.SignInProcess(userDTO));
    }

    @DeleteMapping("/api/signout")
    public ResponseEntity<?> signOut(String userId, String accessToken, HttpServletRequest request) {
        authService.signOut(userId, accessToken);
        return ResponseEntity.ok().body("로그이웃 되었습니다.");
    }

//    @PostMapping("/api/auth/mail/send")
//    public ResponseEntity<?> sendVerificationEmail(String userEmail) {
//        mailVerificationService.sendVerificationEmail(userEmail);
//        return ResponseEntity.ok().body(userEmail + "로 인증번호가 전송 되었습니다.");
//    }
//
//    @PostMapping("/api/auth/mail/verify")
//    public ResponseEntity<?> verifyEmail(String userEmail, String code) {
//        mailVerificationService.verifyCode(userEmail, code);
//        return ResponseEntity.ok().body("이메일 인증이 완료되었습니다");
//    }
}
