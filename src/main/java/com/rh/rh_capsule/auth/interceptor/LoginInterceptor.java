package com.rh.rh_capsule.auth.interceptor;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.support.AuthenticationContext;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final AuthenticationContext authenticationContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthenticationExtractor.extractAccessToken(request)
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHORIZED));

        Long userId = jwtProvider.extractId(accessToken);
        authenticationContext.setAuthentication(userId);

        return true;
    }
}
