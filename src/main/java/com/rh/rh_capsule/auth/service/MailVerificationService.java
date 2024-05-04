package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.SendMailDTO;
import com.rh.rh_capsule.auth.controller.dto.VerificationPurpose;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.redis.RedisDao;
import com.rh.rh_capsule.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisDao redisDao;

    public void sendVerificationEmail(SendMailDTO sendMailDTO) {
        String userEmail = sendMailDTO.userEmail();
        VerificationPurpose purpose = sendMailDTO.purpose();
        switch (purpose){
            case SIGN_UP:
                if (userRepository.existsByUserEmail(userEmail)) {
                    throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
                }
                break;
            case RESET_PASSWORD:
                if (!userRepository.existsByUserEmail(userEmail)) {
                    throw new AuthException(AuthErrorCode.EMAIL_NOT_FOUND);
                }
                break;
        }

        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("RH_CAPSULE 인증 코드입니다");
        message.setText("인증 코드 : " + verificationCode);
        javaMailSender.send(message);

        redisDao.setVerificationCode(userEmail, verificationCode, 3L);
    }

    public void verifyCode(String userEmail, String code) {
        String verificationCode = redisDao.getVerificationCode(userEmail);
        if(!code.equals(verificationCode)){
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE);
        }
        redisDao.setVerification(userEmail, 3L);
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
