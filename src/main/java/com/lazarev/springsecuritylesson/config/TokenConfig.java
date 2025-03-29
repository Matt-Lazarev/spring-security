package com.lazarev.springsecuritylesson.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.util.Base64;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {
    public static final String HEADER = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_PREFIX = "Bearer ";

    private String secret;
    private long expirationMs;

    @Bean
    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
}
