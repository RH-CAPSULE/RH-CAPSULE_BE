package com.rh.rh_capsule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 백엔드의 모든 API 경로에 대해 CORS 설정을 적용합니다.
        registry.addMapping("/**")
                // 특정 프론트엔드 도메인을 명시적으로 허용
//                .allowedOriginPatterns("https://moment-capsule.web.app", "https://write-capsule.xyz")
                .allowedOriginPatterns("*")
                // 허용할 HTTP 메서드 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // JWT 토큰이 포함된 헤더를 허용
                .allowedHeaders("Authorization", "Content-Type")
                // 자격 증명 허용
                .allowCredentials(true);
    }
}
