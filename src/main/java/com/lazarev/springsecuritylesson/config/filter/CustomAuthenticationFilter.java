package com.lazarev.springsecuritylesson.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazarev.springsecuritylesson.config.JwtUtil;
import com.lazarev.springsecuritylesson.config.dto.AuthError;
import com.lazarev.springsecuritylesson.config.dto.AuthRequest;
import com.lazarev.springsecuritylesson.config.dto.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final ObjectMapper MAPPER = new ObjectMapper(); //JSON -> Object, Object -> JSON

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // POST - /login
        if(!request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("POST method is not valid for /login");
        }
        //username, password

        AuthRequest authRequest = MAPPER.readValue(request.getInputStream(), AuthRequest.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.username(),
                authRequest.password()
        );
        return authenticationManager.authenticate(authentication);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = authResult.getName();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        TokenDto tokenDto = new TokenDto(jwtUtil.createAccessToken(username, authorities));

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        MAPPER.writeValue(response.getOutputStream(), tokenDto);
    }

    @Override
    @SneakyThrows
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        AuthError authError = new AuthError("Incorrect username or password");
        MAPPER.writeValue(response.getOutputStream(), authError);
    }
}
