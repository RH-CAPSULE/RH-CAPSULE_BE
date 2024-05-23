package com.rh.rh_capsule.auth.domain;

import java.util.Map;

public class GoogleUser implements OAuthUser{

    private final String id;
    private final String userName;
    private final String userEmail;

    public GoogleUser(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        this.userName = (String) attributes.get("name");
        this.userEmail = (String) attributes.get("email");
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String userName() {
        return userName;
    }

    @Override
    public String userEmail() {
        return userEmail;
    }
}
