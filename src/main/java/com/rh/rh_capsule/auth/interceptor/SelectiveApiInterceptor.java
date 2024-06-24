package com.rh.rh_capsule.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class SelectiveApiInterceptor implements HandlerInterceptor {
    private final HandlerInterceptor handlerInterceptor;
    private final ApiPathMatcher apiPathMatcher;

    public SelectiveApiInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
        this.apiPathMatcher = new ApiPathMatcher();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        if (apiPathMatcher.isExcludePathPattern(request.getServletPath(), request.getMethod())){
            return true;
        }
        return handlerInterceptor.preHandle(request, response, handler);
    }

    public SelectiveApiInterceptor addIncludePattern(String path, HttpMethod... method) {
        apiPathMatcher.addIncludePattern(path, method);
        return this;
    }
    public SelectiveApiInterceptor addExcludePattern(String path, HttpMethod... method) {
        apiPathMatcher.addExcludePattern(path, method);
        return this;
    }
}
