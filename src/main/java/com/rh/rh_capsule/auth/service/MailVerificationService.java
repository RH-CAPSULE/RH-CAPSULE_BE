package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.ErrorCode;
import com.rh.rh_capsule.redis.RedisDao;
import com.rh.rh_capsule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisDao redisDao;

    public void sendVerificationEmail(String userEmail) {
        if (userRepository.existsByUserEmail(userEmail)) {
            throw new AuthException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("RH_CAPSULE 인증 코드입니다");
        message.setText("인증 코드 : " + verificationCode);
        javaMailSender.send(message);

        redisDao.setVerificationCode(userEmail, verificationCode, 3L);
    }

    public String verifyCode(String userEmail, String code) {
        String verificationCode = redisDao.getVerificationCode(userEmail);
        if(!code.equals(verificationCode)){
            throw new AuthException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        redisDao.deleteVerificationCode(userEmail);
        String uuid = UUID.randomUUID().toString();
        redisDao.setVerificationUuid(userEmail, uuid, 3L);
        return uuid;
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
