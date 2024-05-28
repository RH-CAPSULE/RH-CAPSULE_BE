package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.*;
import com.rh.rh_capsule.auth.domain.UserStatus;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.domain.UserAuthority;
import com.rh.rh_capsule.auth.support.AuthenticationExtractor;
import com.rh.rh_capsule.redis.RedisDao;
import com.rh.rh_capsule.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    public void signOut(Long userId, HttpServletRequest request) {
        String accessToken = AuthenticationExtractor.extractAccessToken(request)
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHORIZED));

        redisDao.deleteRefreshToken(userId.toString());
        redisDao.setAccessTokenSignOut(accessToken);
    }

    public void signUp(SignUpDTO signUpDTO) {

        String userEmail = signUpDTO.userEmail();
        String password = signUpDTO.password();
        String name = signUpDTO.userName();


        if(userEmail == null || password == null || name == null){
            throw new AuthException(AuthErrorCode.INVALID_INPUT);
        }

        Boolean isExist = userRepository.existsByUserEmail(userEmail);

        if (isExist) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if(!(redisDao.getVerification(userEmail).equals("Verified"))){
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION);
        }
        redisDao.deleteVerification(userEmail);

        User user = User.builder()
                .userEmail(userEmail)
                .password(bCryptPasswordEncoder.encode(password))
                .userName(name)
                .authority(UserAuthority.NORMAL_USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
    }

    public TokenResponse signIn(UserDTO userDTO) {

        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userEmail));

        if(user.get().getStatus().equals(UserStatus.DELETED)){
            throw new AuthException(AuthErrorCode.DELETED_USER);
        }
        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            return jwtProvider.createTokens(user.get().getId());
        }

        throw new AuthException(AuthErrorCode.UNAUTHORIZED);
    }

    public void resetPassword(UserDTO userDTO) {
        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        if(!(redisDao.getVerification(userEmail) == "Verified")){
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION);
        }
        redisDao.deleteVerification(userEmail);

        User user = userRepository.findByUserEmail(userEmail);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        redisDao.deleteRefreshToken(user.getId().toString());
    }

    public ReissueTokenResponse reissueToken(TokenReissueDTO tokenReissueDTO) {
        if(tokenReissueDTO.refreshToken() == null){
            throw new AuthException(AuthErrorCode.EMPTY_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.extractIdRefresh(tokenReissueDTO.refreshToken());

        String refreshToken = redisDao.getRefreshToken(userId.toString());

        if (refreshToken == null) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if(!refreshToken.equals(tokenReissueDTO.refreshToken())){
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        return jwtProvider.reissueTokens(userId);
    }
}