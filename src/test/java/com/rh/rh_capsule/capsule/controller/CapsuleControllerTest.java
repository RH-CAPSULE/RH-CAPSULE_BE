package com.rh.rh_capsule.capsule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.rh_capsule.auth.interceptor.LoginInterceptor;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.capsule.dto.CapsuleCreateDTO;
import com.rh.rh_capsule.capsule.service.CapsuleService;
import com.rh.rh_capsule.capsule.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CapsuleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CapsuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CapsuleService capsuleService; // 가정: 이 서비스가 컨트롤러에 주입되어 있다고 가정

    @MockBean
    private FileStorageService fileStorageService; // 파일 저장 서비스 모킹


    @Test
    public void testCreateCapsule() throws Exception {
        // JSON 객체를 문자열로 준비
        CapsuleCreateDTO capsuleCreateDTO = new CapsuleCreateDTO("a","a","a","a",1L,true); // 적절하게 초기화 필요
        ObjectMapper objectMapper = new ObjectMapper();
        String capsuleJson = objectMapper.writeValueAsString(capsuleCreateDTO);

        // 멀티파트 파일 생성
        MockMultipartFile jsonFile = new MockMultipartFile("capsule", "", "application/json", capsuleJson.getBytes());
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile audioFile = new MockMultipartFile("audio", "audio.mp3", "audio/mpeg", "test audio content".getBytes());

        // 스토리지 서비스를 모킹
        when(fileStorageService.storeFile(any(), eq("image"))).thenReturn("image-url");
        when(fileStorageService.storeFile(any(), eq("audio"))).thenReturn("audio-url");

        // 컨트롤러 호출
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/capsule/create")
                        .file(jsonFile)
                        .file(imageFile)
                        .file(audioFile)
                        .header("Authorization", "Bearer some-token")) // 테스트용 토큰
                .andExpect(status().isOk())
                .andExpect(content().string("캡슐이 생성되었습니다."));
    }
}