package com.rh.rh_capsule.auth.interceptor;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import com.rh.rh_capsule.redis.RedisDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenBlackListInterceptor implements HandlerInterceptor{
    private final RedisDao redisDao;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        Optional<String> accessToken = AuthenticationExtractor.extractAccessToken(request);
        if (accessToken.isPresent() && redisDao.isKeyOfAccessTokenInBlackList(accessToken.get())) {
            throw new AuthException(AuthErrorCode.ALREADY_SIGN_OUT);
        }
        return true;
    }
}