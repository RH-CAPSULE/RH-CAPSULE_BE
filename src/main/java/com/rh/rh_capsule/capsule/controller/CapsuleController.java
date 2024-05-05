package com.rh.rh_capsule.capsule.controller;

import com.rh.rh_capsule.auth.support.AuthUser;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.service.CapsuleService;
import com.rh.rh_capsule.capsule.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CapsuleController {

    private final CapsuleService capsuleService;
    private final FileStorageService fileStorageService;
    @PostMapping("/api/capsule-box/create")
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

    @PostMapping(value = "/api/capsule/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createCapsule(
            @RequestPart("capsule") @Schema(implementation = CapsuleCreateDTO.class)CapsuleCreateDTO capsuleCreateDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "audio", required = false) MultipartFile audio) {

        String imageUrl = null;
        String audioUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image, "image");
        }
        if (audio != null && !audio.isEmpty()) {
            audioUrl = fileStorageService.storeFile(audio, "audio");
        }
        capsuleService.createCapsule(capsuleCreateDTO, imageUrl, audioUrl);
        return ResponseEntity.ok().body("캡슐이 생성되었습니다.");
    }
}
