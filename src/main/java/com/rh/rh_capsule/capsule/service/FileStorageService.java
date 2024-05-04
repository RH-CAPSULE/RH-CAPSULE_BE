package com.rh.rh_capsule.capsule.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService() {
        // 정적 폴더에 대한 경로 설정
        this.rootLocation = Paths.get("/Users/dohun/대학/프로젝트/files");

        // 디렉토리 생성
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new RuntimeException("파일을 저장할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    public String storeFile(MultipartFile file, String type) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("빈 파일은 저장할 수 없습니다.");
            }

            String filename = file.getOriginalFilename();
            String fileExtension = filename.substring(filename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;  // UUID를 파일명으로 사용

            // type에 따라 서브 디렉토리를 생성합니다.
            Path typePath = this.rootLocation.resolve(type);
            Files.createDirectories(typePath);

            // 파일 저장 경로를 계산합니다.
            Path destinationFile = typePath.resolve(newFilename).normalize().toAbsolutePath();

            // 파일을 실제로 저장합니다.
            file.transferTo(destinationFile);

            // 웹에서 접근 가능한 상대 URL 반환
            return "/files/" + type + "/" + newFilename;
        } catch (Exception e) {
            throw new RuntimeException("파일을 저장하지 못했습니다.", e);
        }
    }
}
