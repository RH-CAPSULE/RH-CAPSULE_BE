package com.rh.rh_capsule.inquiry.controller;

import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.inquiry.controller.dto.InquiryDTO;
import com.rh.rh_capsule.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/api/inquiry")
    public ResponseEntity<?> createInquiry(@RequestBody InquiryDTO inquiryDTO,
                                           @Parameter(hidden = true) @AuthUser Long userId) {
        inquiryService.createInquiry(inquiryDTO, userId);
        return ResponseEntity.ok().body("문의가 성공적으로 등록되었습니다.");
    }
}
