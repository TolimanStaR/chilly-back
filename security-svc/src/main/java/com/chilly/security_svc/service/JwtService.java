package com.chilly.security_svc.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    private static final String ROLES_KEY = "roles";

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    @Getter
    private long jwtExpiration;

    @Value("${http.headers.user-id}")
    private String userIdHeader;

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    public String generateToken(UserDetails userDetails, Long userId) {
        log.info("generating token with extra claim ({}, {})", userIdHeader, userId);
        Map<String, Object> extras = Map.ofEntries(
                Map.entry(userIdHeader, userId),
                Map.entry(ROLES_KEY, serializeAuthorities(userDetails))
        );
        return generateToken(extras, userDetails);
    }

    private String buildToken(Map<String, Object> extras, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extras)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(decodeSecretKey())
                .compact();

    }

    private SecretKey decodeSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String serializeAuthorities(UserDetails userDetails) {
        return String.join(
                ",",
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

}
