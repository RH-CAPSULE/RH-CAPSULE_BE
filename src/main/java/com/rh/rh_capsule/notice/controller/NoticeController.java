package com.rh.rh_capsule.notice.controller;

import com.rh.rh_capsule.notice.controller.dto.NoticeDTO;
import com.rh.rh_capsule.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    @GetMapping("/api/notice")
    public ResponseEntity<NoticeDTO> getLatestNotice() {
        return ResponseEntity.ok(noticeService.getLatestNotice());
    }

}
