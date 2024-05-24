package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.controller.dto.*;
import com.rh.rh_capsule.auth.controller.dto.UserDTO;
import com.rh.rh_capsule.auth.service.AuthService;
import com.rh.rh_capsule.auth.support.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/sign-up")
    @Operation(summary = "회원가입", description = "회원가입을 수행하기 전에 이메일 인증을 먼저 진행해야합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않습니다."),
            @ApiResponse(responseCode = "401", description = "이메일 인증이 완료되지 않았습니다."),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일입니다.")
    })
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/auth/sign-in")
    @Operation(summary = "로그인", description = "로그인을 수행합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "로그인 되었습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    public ResponseEntity<TokenResponse> signIn(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.signIn(userDTO));
    }

    @DeleteMapping("/api/auth/sign-out")
    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃 되었습니다."),
            @ApiResponse(responseCode = "401", description = "")
    })
    public ResponseEntity<?> signOut(@Parameter(hidden = true) @AuthUser Long userId, HttpServletRequest request) {
        authService.signOut(userId, request);
        return ResponseEntity.ok().body("로그이웃 되었습니다.");
    }

    @PostMapping("/api/auth/reset-pw")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다, 이메일 인증을 먼저 진행해야합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 재설정되었습니다."),
            @ApiResponse(responseCode = "401", description = "이메일 인증이 완료되지 않았습니다.")
    })
    public ResponseEntity<String> resetPassword(@RequestBody UserDTO userDTO) {
        authService.resetPassword(userDTO);
        return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
    }

    @PostMapping("/api/auth/token-reissue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "토큰이 재발급되었습니다."),
            @ApiResponse(responseCode = "400", description = "리프레시 토큰이 올바르지 않습니다."),
    })
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody TokenReissueDTO tokenReissueDTO,
                                                             @Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok(authService.reissueToken(userId, tokenReissueDTO));
    }
}