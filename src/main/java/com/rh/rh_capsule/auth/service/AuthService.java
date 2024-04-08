package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.dto.SignUpDTO;
import com.rh.rh_capsule.auth.dto.TokenResponse;
import com.rh.rh_capsule.auth.dto.UserDTO;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.ErrorCode;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.domain.User;
import com.rh.rh_capsule.domain.UserAuthority;
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

    public void signOut(String userId, String accessToken) {
        redisDao.deleteRefreshToken(userId);
        redisDao.setAccessTokenSignOut(accessToken);
    }

    public Long SignUpProcess(SignUpDTO signUpDTO) {

        String userEmail = signUpDTO.userEmail();
        String password = signUpDTO.password();
        String uuid = signUpDTO.uuid();
        Boolean isExist = userRepository.existsByUserEmail(userEmail);

        if (isExist) {
            throw new AuthException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        //이 부분 널처리 해야함
        System.out.println("uuid : " + uuid);
        System.out.println("redis uuid : " + redisDao.getVerificationUuid(userEmail));
        if(!uuid.equals(redisDao.getVerificationUuid(userEmail))){
            throw new AuthException(ErrorCode.INVALID_VERIFICATION_UUID);
        }

        //이메인 인증 구현
        User data = new User();

        data.setUserEmail(userEmail);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setAuthority(UserAuthority.NORMAL_USER);

        userRepository.save(data);
        return userRepository.findByUserEmail(userEmail).getId();
    }

    public TokenResponse SignInProcess(UserDTO userDTO) {

        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userEmail));

        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            return jwtProvider.createTokens(user.get().getId());
        }

        throw new AuthException(ErrorCode.UNAUTHORIZED);
    }
}