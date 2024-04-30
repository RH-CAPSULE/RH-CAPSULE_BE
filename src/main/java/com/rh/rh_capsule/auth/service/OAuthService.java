package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.TokenResponse;
import com.rh.rh_capsule.auth.domain.OAuthUser;
import com.rh.rh_capsule.auth.domain.Provider;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.domain.UserAuthority;
import com.rh.rh_capsule.auth.infrastructure.RestTemplateOAuthRequester;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtProvider jwtProvider;
    private final RestTemplateOAuthRequester restTemplateOAuthRequester;
    private final UserRepository userRepository;

    private static final String OAUTH_PASSWORD = "oauth";
    public String signInUri(String redirectUri, String provider) {
        return restTemplateOAuthRequester.signInUri(Provider.from(provider), redirectUri);
    }

    public TokenResponse signIn(OAuthUser oAuthUser) {
        String userEmail = oAuthUser.userEmail();
        if(userRepository.existsByUserEmail(userEmail)){
            User user = userRepository.findByUserEmail(userEmail);
            return jwtProvider.createTokens(user.getId());
        }
        User newUser = new User();
        System.out.println("userEmail = " + userEmail);
        newUser.setUserEmail(userEmail);
        newUser.setPassword(OAUTH_PASSWORD);
        newUser.setUsername(oAuthUser.username());
        newUser.setAuthority(UserAuthority.NORMAL_USER);
        userRepository.save(newUser);
        return jwtProvider.createTokens(newUser.getId());
    }
}
