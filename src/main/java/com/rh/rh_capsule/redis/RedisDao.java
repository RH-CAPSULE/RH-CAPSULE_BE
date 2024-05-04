package com.rh.rh_capsule.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;
    private final String SIGN_OUT_VALUE = "SIGN_OUT_VALUE";

    public void setRefreshToken(String userId, String refreshToken, long refreshTokenTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.opsForValue().set(userId, refreshToken, refreshTokenTime, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userId){
        return redisTemplate.opsForValue().get(userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }

    public void setAccessTokenSignOut(String accessToken){
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.opsForValue().set(accessToken, SIGN_OUT_VALUE, 1L, TimeUnit.DAYS);
    }

    public boolean isKeyOfAccessTokenInBlackList(String accessToken) {
        String signOutValue = redisTemplate.opsForValue().get(accessToken);
        return SIGN_OUT_VALUE.equals(signOutValue);
    }

    public void setVerificationCode(String mail, String code, Long codeTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.opsForValue().set(mail, code, codeTime, TimeUnit.MINUTES);
    }

    public String getVerificationCode(String mail) {
        return redisTemplate.opsForValue().get(mail);
    }

    public void setVerification(String userEmail, Long uuidTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.opsForValue().set(userEmail,"Verified", uuidTime, TimeUnit.MINUTES);
    }

    public String getVerification(String mail) {
        return redisTemplate.opsForValue().get(mail);
    }

    public void deleteVerification(String userEmail) {
        redisTemplate.delete(userEmail);
    }

    public void flushAll(){
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
