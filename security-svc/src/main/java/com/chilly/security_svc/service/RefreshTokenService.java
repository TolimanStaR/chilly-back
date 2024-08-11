package com.chilly.security_svc.service;

import com.chilly.security_svc.model.RefreshToken;
import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${security.refresh-token.expiration-time}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.save(buildToken());
        user.setRefreshToken(refreshToken);
        return refreshToken.getToken();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiration().before(new Date());
    }

    private RefreshToken buildToken() {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .build();
    }
}
