package com.rh.rh_capsule.auth.config;

import com.rh.rh_capsule.auth.interceptor.LoginInterceptor;
import com.rh.rh_capsule.auth.interceptor.TokenBlackListInterceptor;
import com.rh.rh_capsule.auth.interceptor.TokenExistenceInterceptor;
import com.rh.rh_capsule.auth.support.AuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

    private final AuthArgumentResolver authArgumentResolver;



    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenExistenceInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/oauth/**", "/swagger-ui/**", "/v3/**", "/index.html");
        registry.addInterceptor(tokenBlackListInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/oauth/**", "/swagger-ui/**", "/v3/**", "/index.html");
        registry.addInterceptor(loginInterceptor)
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/oauth/**", "/swagger-ui/**", "/v3/**", "/index.html");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
