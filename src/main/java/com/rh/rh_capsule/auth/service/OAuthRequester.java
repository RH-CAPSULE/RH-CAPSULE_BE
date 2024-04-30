package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.domain.OAuthUser;
import com.rh.rh_capsule.auth.domain.Provider;
import com.rh.rh_capsule.auth.service.dto.OAuthSignInRequest;

public interface OAuthRequester {
    String signInUri(Provider provider, String redirectUri);

    OAuthUser signIn(OAuthSignInRequest request, String provider);
}
