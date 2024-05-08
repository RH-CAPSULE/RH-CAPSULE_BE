package com.rh.rh_capsule.auth.interceptor;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenExistenceInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        Optional<String> accessToken = AuthenticationExtractor.extractAccessToken(request);
        if(!accessToken.isPresent()){
            throw new AuthException(AuthErrorCode.TOKEN_EMPTY);
        }
        return true;
    }
}
