package com.rh.rh_capsule.auth.config;

import com.rh.rh_capsule.auth.interceptor.*;
import com.rh.rh_capsule.auth.support.AuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.rh.rh_capsule.auth.interceptor.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final SignInInterceptor signInInterceptor;
    private final TokenBlackListInterceptor tokenBlackListInterceptor;
    private final TokenReissueInterceptor tokenReissueInterceptor;

    private final AuthArgumentResolver authArgumentResolver;



    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenBlackListInterceptor())
                .order(1);
        registry.addInterceptor(signInInterceptor())
                .order(2);
        registry.addInterceptor(tokenReissueInterceptor())
                .order(3);

    }


    private HandlerInterceptor tokenBlackListInterceptor() {
        return new SelectiveApiInterceptor(tokenBlackListInterceptor)
                .addExcludePattern("/**", OPTIONS)

                .addIncludePattern("/api/auth/user", GET)
                .addIncludePattern("/api/auth/sign-out", DELETE)
                .addIncludePattern("/api/capsule-box", GET)
                .addIncludePattern("/api/capsule-box", POST)
                .addIncludePattern("/api/capsule-box/**", DELETE)
                .addIncludePattern("/api/history-capsule-boxes", GET)
                .addIncludePattern("/api/capsule-list/**", GET)
                .addIncludePattern("/api/capsule/**", GET)
                .addIncludePattern("/api/capsule", POST)

                .addExcludePattern("/api/auth/token-reissue", POST)
                ;
    }

    private HandlerInterceptor signInInterceptor() {
        return new SelectiveApiInterceptor(signInInterceptor)
                .addExcludePattern("/**", OPTIONS)

                .addIncludePattern("/api/auth/user", GET)
                .addIncludePattern("/api/auth/sign-out", DELETE)
                .addIncludePattern("/api/capsule-box", GET)
                .addIncludePattern("/api/capsule-box", POST)
                .addIncludePattern("/api/capsule-box/**", DELETE)
                .addIncludePattern("/api/history-capsule-boxes", GET)
                .addIncludePattern("/api/capsule-list/**", GET)
                .addIncludePattern("/api/capsule/**", GET)
                .addIncludePattern("/api/capsule", POST)

                .addExcludePattern("/api/auth/token-reissue", POST)
                ;
    }

    private HandlerInterceptor tokenReissueInterceptor() {
        return new SelectiveApiInterceptor(tokenReissueInterceptor)
                .addExcludePattern("/**", OPTIONS)

                .addIncludePattern("/api/auth/token-reissue", POST)
                ;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
