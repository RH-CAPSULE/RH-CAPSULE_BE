package com.rh.rh_capsule.capsule.controller;

import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.service.CapsuleService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CapsuleController {

    private final CapsuleService capsuleService;

    @PostMapping("/api/capsule-box")
    public ResponseEntity<?> createCapsuleBox(@RequestBody CapsuleBoxCreateDTO capsuleBoxCreateDTO, @AuthUser Long userId) {
        capsuleService.createCapsuleBox(capsuleBoxCreateDTO, userId);
        return ResponseEntity.ok().body("캡슐함이 생성되었습니다.");
    }

    @GetMapping("/api/capsule-box")
    public ResponseEntity<ActiveCapsuleBoxDto> getActiveCapsuleBox(@AuthUser Long userId) {
        ActiveCapsuleBoxDto capsuleBoxList = capsuleService.getCapsuleBoxList(userId);
        return ResponseEntity.ok().body(capsuleBoxList);
    }

    @GetMapping("/api/history-capsule-boxes")
    public ResponseEntity<List<HistoryCapsuleBoxes>> getHistoryCapsuleBoxes(@AuthUser Long userId, @RequestBody PaginationDTO paginationDTO) {
        return ResponseEntity.ok().body(capsuleService.getHistoryCapsuleBoxes(userId, paginationDTO));
    }

    @DeleteMapping("/api/capsule-box/{capsuleBoxId}")
    public ResponseEntity<?> deleteCapsuleBox(@PathVariable Long capsuleBoxId) {
        capsuleService.deleteCapsuleBox(capsuleBoxId);
        return ResponseEntity.ok().body("캡슐함이 삭제되었습니다.");
    }
    @PostMapping(value = "/api/capsule", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCapsule(@RequestPart(value = "capsule") @Schema(implementation = CapsuleCreateDTO.class) CapsuleCreateDTO capsuleCreateDTO,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestPart(value = "audio", required = false) MultipartFile audio,
                                           HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);

        }
        capsuleService.createCapsule(capsuleCreateDTO, image, audio, token);
        return ResponseEntity.ok().body("캡슐이 생성되었습니다.");
    }
    @GetMapping("/api/capsule-list/{capsuleBoxId}")
    public ResponseEntity<List<CapsuleListDTO>> getCapsuleList(@PathVariable Long capsuleBoxId, @RequestBody PaginationDTO paginationDTO) {
        return ResponseEntity.ok().body(capsuleService.getCapsuleList(capsuleBoxId, paginationDTO));
    }

    @GetMapping("/api/capsule/{capsuleId}")
    public ResponseEntity<CapsuleDTO> getCapsule(@PathVariable Long capsuleId) {
        return ResponseEntity.ok().body(capsuleService.getCapsule(capsuleId));
    }
}
