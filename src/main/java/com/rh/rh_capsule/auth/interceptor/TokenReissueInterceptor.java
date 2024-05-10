package com.rh.rh_capsule.auth.interceptor;

import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.support.AuthenticationContext;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class TokenReissueInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final AuthenticationContext authenticationContext;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthenticationExtractor.extractAccessToken(request).get();

        Long userId = jwtProvider.extractIdIgnoringExpiration(accessToken);
        authenticationContext.setAuthentication(userId);

        return true;
    }
}
