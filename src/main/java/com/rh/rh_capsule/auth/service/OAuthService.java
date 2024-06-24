package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.OAuthSignInResponse;
import com.rh.rh_capsule.auth.controller.dto.TokenResponse;
import com.rh.rh_capsule.auth.domain.*;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.infrastructure.RestTemplateOAuthRequester;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.repository.UserRepository;
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

    public OAuthSignInResponse signIn(OAuthUser oAuthUser) {
        String userEmail = oAuthUser.userEmail();
        if(userRepository.existsByUserEmail(userEmail)){
            User user = userRepository.findByUserEmail(userEmail).get();
            Boolean isFirstSignIn = user.getStatus().equals(UserStatus.JOINED);

            if(user.getStatus().equals(UserStatus.DELETED)){
                throw new AuthException(AuthErrorCode.DELETED_USER);
            }
            TokenResponse tokens = jwtProvider.createTokens(user.getId());
            return new OAuthSignInResponse(tokens.accessToken(), tokens.refreshToken(), isFirstSignIn);
        }
        User user = User.builder()
                .userEmail(userEmail)
                .userName(oAuthUser.userName())
                .password(OAUTH_PASSWORD)
                .authority(UserAuthority.NORMAL_USER)
                .status(UserStatus.JOINED)
                .build();
        userRepository.save(user);
        TokenResponse tokens = jwtProvider.createTokens(user.getId());
        return new OAuthSignInResponse(tokens.accessToken(), tokens.refreshToken(), true);
    }
}
