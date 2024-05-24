package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.controller.dto.UserDetailDTO;
import com.rh.rh_capsule.auth.controller.dto.UserUpdate;
import com.rh.rh_capsule.auth.service.AuthService;
import com.rh.rh_capsule.auth.service.UserService;
import com.rh.rh_capsule.auth.support.AuthUser;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    @GetMapping("/api/user")
    public ResponseEntity<UserDetailDTO> getUser(@Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }
    @DeleteMapping("/api/user/resign")
    public ResponseEntity<?> resign(@Parameter(hidden = true) @AuthUser Long userId) {
        userService.resign(userId);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }
    @PatchMapping("/api/user")
    public ResponseEntity<?> updateUser(@Parameter(hidden = true) @AuthUser Long userId, UserUpdate userUpdate) {
        userService.updateUser(userId, userUpdate);
        return ResponseEntity.ok().body("회원 정보가 수정되었습니다.");
    }
}
