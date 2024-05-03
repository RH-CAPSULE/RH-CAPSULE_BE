package com.rh.rh_capsule.auth.domain;

import java.util.Map;

public class GoogleUser implements OAuthUser{

    private final String id;
    private final String username;
    private final String userEmail;

    public GoogleUser(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        this.username = (String) attributes.get("name");
        this.userEmail = (String) attributes.get("email");
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String userEmail() {
        return userEmail;
    }
}
