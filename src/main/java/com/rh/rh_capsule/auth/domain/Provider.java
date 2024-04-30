package com.rh.rh_capsule.auth.domain;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.ErrorCode;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum Provider {
    GOOGLE("google", GoogleUser::new)
    ;

    private final String providerName;
    private final Function<Map<String, Object>, OAuthUser> function;

    Provider(String providerName, Function<Map<String, Object>, OAuthUser> function) {
        this.providerName = providerName;
        this.function = function;
    }

    public static Provider from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.providerName.equals(name))
                .findFirst()
                .orElseThrow(() -> new AuthException(ErrorCode.UNAUTHORIZED));
        //에러 코드 변경 할 것
    }

    public OAuthUser getOAuthUser(Map<String, Object> body) {
        return function.apply(body);
    }
}
