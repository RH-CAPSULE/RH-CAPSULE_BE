package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.*;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.domain.UserAuthority;
import com.rh.rh_capsule.redis.RedisDao;
import com.rh.rh_capsule.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    public void signOut(Long userId, String accessToken) {
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

        //이메인 인증 구현
        User data = new User();

        data.setUserEmail(userEmail);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setUserName(name);
        data.setAuthority(UserAuthority.NORMAL_USER);

        userRepository.save(data);
    }

    public TokenResponse signIn(UserDTO userDTO) {

        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userEmail));

        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            return jwtProvider.createTokens(user.get().getId());
        }

        throw new AuthException(AuthErrorCode.UNAUTHORIZED);
    }

    public void resetPassword(UserDTO userDTO) {
        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        //이 부분 널처리 해야함
        if(!(redisDao.getVerification(userEmail) == "Verified")){
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION);
        }
        redisDao.deleteVerification(userEmail);

        User user = userRepository.findByUserEmail(userEmail);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        redisDao.deleteRefreshToken(user.getId().toString());
    }

    public ReissueTokenResponse reissueToken(Long userId, TokenReissueDTO tokenReissueDTO) {
        if(!jwtProvider.isRefreshTokenExpired(tokenReissueDTO.refreshToken())){
            String refreshToken = redisDao.getRefreshToken(userId.toString());
            if(!refreshToken.equals(tokenReissueDTO.refreshToken())){
                throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
            }
        }
        return jwtProvider.reissueTokens(userId);
    }
}