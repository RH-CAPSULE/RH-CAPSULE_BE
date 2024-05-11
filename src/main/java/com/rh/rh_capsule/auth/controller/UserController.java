package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.controller.dto.UserDetailDTO;
import com.rh.rh_capsule.auth.service.UserService;
import com.rh.rh_capsule.auth.support.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/api/auth/user")
    public ResponseEntity<UserDetailDTO> getUser(@AuthUser Long userId) {

        return ResponseEntity.ok().body(userService.getUser(userId));
    }
}
