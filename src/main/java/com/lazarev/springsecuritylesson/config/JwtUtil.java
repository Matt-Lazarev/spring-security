package com.lazarev.springsecuritylesson.config;

import com.lazarev.springsecuritylesson.config.dto.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final TokenConfig tokenConfig;
    private final SecretKey secretKey;

    public String createAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
        String token = Jwts.builder()
                .issuer("security-jwt-application")
                .issuedAt(new Date())
                .expiration(new Date( new Date().getTime() + tokenConfig.getExpirationMs() ))
                .subject(username)
                .claim("authorities", authorities)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return TokenConfig.TOKEN_PREFIX + token;
    }

    public UserInfo validateAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        return new UserInfo(claims.getSubject(), parseAuthorities((claims.get("authorities"))));
    }

    @SuppressWarnings("unchecked")
    private Set<? extends GrantedAuthority> parseAuthorities(Object authoritiesClaim) {
        List<Map<String, String>> authorities = (List<Map<String, String>>) authoritiesClaim;
        return authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());
    }
}
