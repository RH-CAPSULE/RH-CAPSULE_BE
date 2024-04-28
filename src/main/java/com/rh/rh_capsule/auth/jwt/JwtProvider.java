package com.rh.rh_capsule.auth.jwt;

import com.rh.rh_capsule.auth.dto.TokenResponse;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.ErrorCode;
import com.rh.rh_capsule.redis.RedisDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final RedisDao redisDao;

    private static final Long ONE_DAY = 60 * 60 * 24L;
    private static final Long ONE_HOUR = 60 * 60L;
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = ONE_HOUR * 1L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = ONE_DAY * 7L;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenResponse createTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String createAccessToken(Long id) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        return accessToken(claims);
    }

    private String createRefreshToken(Long id) {
        Claims claims = Jwts.claims();
        claims.put("id", id);

        String refreshToken = refreshToken(claims);
        redisDao.setRefreshToken(String.valueOf(id), refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);
        return refreshToken;
    }

    private String accessToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt())
                .setExpiration(accessTokenExpiredAt())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String refreshToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt())
                .setExpiration(refreshTokenExpiredAt())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date issuedAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date accessTokenExpiredAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date refreshTokenExpiredAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Long extractId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("id", Long.class);
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.EXPIRED_TOKEN);
        } catch (SecurityException e) {
            throw new AuthException(ErrorCode.SECURITY_ERROR);
        } catch (MalformedJwtException e) {
            throw new AuthException(ErrorCode.MALFORMED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new AuthException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN_FORMAT);
        } catch (JwtException e){
            //위애서 안걸린 jwt 기타 익셉션
            throw new AuthException(ErrorCode.JWT_ERROR);
        }
    }

    //재발급시에만 사용
    public Long extractIdIgnoringExpiration(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("id", Long.class);
        } catch (ExpiredJwtException e) {
            Claims expiredClaims = e.getClaims(); //catch 후 id 반환하고 이를 사용해 엑세스 토큰을 추출할 수 있음
            return expiredClaims.get("id", Long.class);
        } catch (SecurityException e) {
            throw new RuntimeException();
        } catch (MalformedJwtException e) {
            //익셉션 만들어서 던지기
            throw new RuntimeException();
        } catch (UnsupportedJwtException e) {
            //익셉션 만들어서 던지기
            throw new RuntimeException();
        } catch (IllegalArgumentException e) {
            //익셉션 만들어서 던지기
            throw new RuntimeException();
        }
    }

}
