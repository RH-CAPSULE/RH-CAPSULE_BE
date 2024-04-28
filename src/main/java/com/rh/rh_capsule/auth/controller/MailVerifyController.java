package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.dto.SendMailDTO;
import com.rh.rh_capsule.auth.dto.VerifyMailDTO;
import com.rh.rh_capsule.auth.service.MailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailVerifyController {

    private final MailVerificationService mailVerificationService;

    @PostMapping("/api/auth/mail/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody SendMailDTO sendMailDTO) {
        mailVerificationService.sendVerificationEmail(sendMailDTO);
        return ResponseEntity.ok().body(sendMailDTO.userEmail() + "로 인증번호가 전송 되었습니다.");
    }

    @PostMapping("/api/auth/mail/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyMailDTO verifyMailDTO) {
        mailVerificationService.verifyCode(verifyMailDTO.userEmail(), verifyMailDTO.code());
        return ResponseEntity.ok().body("인증이 완료되었습니다.");
    }
}
