package com.yzgeneration.evc.domain.member.authentication.implement;

import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.port.RefreshTokenRepository;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class TokenProvider {

    private final SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final Long ACCESS_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;
    public static final Long REFRESH_TOKEN_VALID_TIME = 1000L * 60L * 60L * 24L * 30L;

    public TokenProvider(@Value("${SECRET_KEY}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthenticationToken create(Long memberId) {
        String accessToken = generateAccessToken(memberId);
        String refreshToken = generateRefreshToken(memberId);
        return new AuthenticationToken(accessToken, refreshToken);
    }

    public Long getMemberId(String accessToken) {
        Claims claims = getClaims(accessToken);
        return claims.get("memberId", Long.class);
    }

    public AuthenticationToken refresh(String refreshToken) {
        Claims claims = validRefreshToken(refreshToken);
        Long memberId = claims.get("memberId", Long.class);
        String accessToken = generateAccessToken(memberId);
        if (isNearExpiration(claims.getExpiration())) {
            return AuthenticationToken.create(accessToken, generateRefreshToken(memberId));
        }
        return AuthenticationToken.create(accessToken, refreshToken);
    }

    private String generateAccessToken(Long memberId) {
        Claims claims = Jwts.claims().
                subject("accessToken")
                .add("memberId", memberId)
                .build();
        Date currentTime = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(currentTime)
                .expiration(new Date(currentTime.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key)
                .compact();
    }

    private String generateRefreshToken(Long memberId) {
        Claims claims = Jwts.claims()
                .subject("refreshToken")
                .add("memberId", memberId)
                .build();
        Date currentTime = new Date();

        String refreshToken = Jwts.builder()
                .claims(claims)
                .issuedAt(currentTime)
                .expiration(new Date(currentTime.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(key)
                .compact();
        refreshTokenRepository.save(memberId, refreshToken);
        return refreshToken;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims validRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        if (!claims.getSubject().equals("refreshToken")) {
            throw new CustomException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        Long memberId = claims.get("memberId", Long.class);
        String storedToken = refreshTokenRepository.getByMemberId(memberId);
        if (!storedToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        return claims;
    }

    private boolean isNearExpiration(Date expiration) {
        Date now = new Date();
        long differenceInMillis = expiration.getTime() - now.getTime();
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
        return differenceInDays <= 7;
    }

}
