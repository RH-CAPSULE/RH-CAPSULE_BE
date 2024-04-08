package com.rh.rh_capsule.auth.config;

import com.rh.rh_capsule.auth.interceptor.LoginInterceptor;
import com.rh.rh_capsule.auth.interceptor.TokenBlackListInterceptor;
import com.rh.rh_capsule.auth.interceptor.TokenExistenceInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final LoginInterceptor loginInterceptor;
    private final TokenBlackListInterceptor tokenBlackListInterceptor;
    private final TokenExistenceInterceptor tokenExistenceInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenExistenceInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/swagger-ui/**", "/v3/**");
        registry.addInterceptor(tokenBlackListInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/swagger-ui/**", "/v3/**");
        registry.addInterceptor(loginInterceptor)
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/swagger-ui/**", "/v3/**");

    }
}
