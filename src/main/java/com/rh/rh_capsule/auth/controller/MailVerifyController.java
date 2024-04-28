package com.rh.rh_capsule.auth.controller;

import com.rh.rh_capsule.auth.dto.SendMailDTO;
import com.rh.rh_capsule.auth.dto.VerifyMailDTO;
import com.rh.rh_capsule.auth.dto.VerifyResponseDTO;
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
    public ResponseEntity<VerifyResponseDTO> verifyEmail(@RequestBody VerifyMailDTO verifyMailDTO) {
        String uuid = mailVerificationService.verifyCode(verifyMailDTO.userEmail(), verifyMailDTO.code());
        VerifyResponseDTO verifyResponseDTO = new VerifyResponseDTO(uuid);
        return ResponseEntity.ok(verifyResponseDTO);
    }
}
