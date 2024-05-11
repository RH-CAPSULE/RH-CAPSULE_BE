package com.rh.rh_capsule.auth.interceptor;


import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;


public class ApiPathMatcher {
    private final PathMatcher pathMatcher;
    private final List<PathPattern> includePatterns;
    private final List<PathPattern> excludePatterns;

    public ApiPathMatcher() {
        this.pathMatcher = new AntPathMatcher();
        this.includePatterns = new ArrayList<>();
        this.excludePatterns = new ArrayList<>();
    }

    public boolean isExcludePathPattern(String targetPath, String method) {
        boolean isExcludePattern = excludePatterns.stream()
                .anyMatch(pattern -> pattern.matches(pathMatcher, targetPath, method));

        boolean isIncludePattern = includePatterns.stream()
                .anyMatch(pattern -> pattern.matches(pathMatcher, targetPath, method));


        return isExcludePattern || !isIncludePattern;
    }
    public void addIncludePattern(String path, HttpMethod... method) {
        for (HttpMethod httpMethod : method) {
            includePatterns.add(new PathPattern(path, httpMethod));
        }
    }

    public void addExcludePattern(String path, HttpMethod... method) {
        for (HttpMethod httpMethod : method) {
            excludePatterns.add(new PathPattern(path, httpMethod));
        }
    }
}
