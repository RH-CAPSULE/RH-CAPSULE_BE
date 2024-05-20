package com.rh.rh_capsule.capsule.controller;

import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.service.CapsuleService;
import com.rh.rh_capsule.capsule.service.dto.UserType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CapsuleController {

    private final CapsuleService capsuleService;

    @PostMapping("/api/capsule-box")
    public ResponseEntity<?> createCapsuleBox(@RequestBody CapsuleBoxCreateDTO capsuleBoxCreateDTO, @Parameter(hidden = true) @AuthUser Long userId) {
        capsuleService.createCapsuleBox(capsuleBoxCreateDTO, userId);
        return ResponseEntity.ok().body("캡슐함이 생성되었습니다.");
    }

    @GetMapping("/api/capsule-box")
    public ResponseEntity<ActiveCapsuleBoxDTO> getActiveCapsuleBox(@Parameter(hidden = true) @AuthUser Long userId) {
        ActiveCapsuleBoxDTO capsuleBoxList = capsuleService.getCapsuleBoxList(userId);
        return ResponseEntity.ok().body(capsuleBoxList);
    }

    @GetMapping("/api/history-capsule-boxes")
    public ResponseEntity<PagedContent<HistoryCapsuleBoxes>> getHistoryCapsuleBoxes(@Parameter(hidden = true) @AuthUser Long userId,
                                                                            @RequestParam int page,
                                                                            @RequestParam int size) {
        return ResponseEntity.ok().body(capsuleService.getHistoryCapsuleBoxes(userId, page, size));
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
                                           @Parameter(hidden = true) @AuthUser Long id) {

        capsuleService.createCapsuleProcess(capsuleCreateDTO, image, audio, UserType.USER, id);
        return ResponseEntity.ok().body("캡슐이 생성되었습니다.");
    }

    @PostMapping(value = "/api/guest/capsule", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createGuestCapsule(@RequestPart(value = "capsule") @Schema(implementation = CapsuleCreateDTO.class) CapsuleCreateDTO capsuleCreateDTO,
                                                @RequestPart(value = "image", required = false) MultipartFile image,
                                                @RequestPart(value = "audio", required = false) MultipartFile audio) {
        capsuleService.createCapsuleProcess(capsuleCreateDTO, image, audio, UserType.GUEST, null);
        return ResponseEntity.ok().body("캡슐이 생성되었습니다.");
    }
    @GetMapping("/api/capsule-list/{capsuleBoxId}")
    public ResponseEntity<PagedContent> getCapsuleList(@PathVariable Long capsuleBoxId,
                                                       @Parameter(hidden = true) @AuthUser Long userId,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        return ResponseEntity.ok().body(capsuleService.getCapsuleList(capsuleBoxId, userId, page, size));
    }

    @GetMapping("/api/capsule/{capsuleId}")
    public ResponseEntity<CapsuleDTO> getCapsule(@PathVariable Long capsuleId) {
        return ResponseEntity.ok().body(capsuleService.getCapsule(capsuleId));
    }
}
