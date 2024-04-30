package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.UserDTO;
import com.rh.rh_capsule.auth.controller.dto.TokenResponse;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.ErrorCode;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.domain.UserAuthority;
import com.rh.rh_capsule.redis.RedisDao;
import com.rh.rh_capsule.repository.UserRepository;
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

    public Long signUp(UserDTO userDTO) {

        String userEmail = userDTO.userEmail();
        String password = userDTO.password();
        Boolean isExist = userRepository.existsByUserEmail(userEmail);

        if (isExist) {
            throw new AuthException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if(!(redisDao.getVerification(userEmail) == "Verified")){
            throw new AuthException(ErrorCode.INVALID_VERIFICATION);
        }
        redisDao.deleteVerification(userEmail);

        //이메인 인증 구현
        User data = new User();

        data.setUserEmail(userEmail);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setAuthority(UserAuthority.NORMAL_USER);

        userRepository.save(data);
        return userRepository.findByUserEmail(userEmail).getId();
    }

    public TokenResponse signIn(UserDTO userDTO) {

        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userEmail));

        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            return jwtProvider.createTokens(user.get().getId());
        }

        throw new AuthException(ErrorCode.UNAUTHORIZED);
    }

    public void resetPassword(UserDTO userDTO) {
        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        //이 부분 널처리 해야함
        if(!(redisDao.getVerification(userEmail) == "Verified")){
            throw new AuthException(ErrorCode.INVALID_VERIFICATION);
        }
        redisDao.deleteVerification(userEmail);

        User user = userRepository.findByUserEmail(userEmail);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        redisDao.deleteRefreshToken(user.getId().toString());
    }
}