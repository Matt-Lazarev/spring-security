package com.lazarev.springsecuritylesson.config.filter;

import com.lazarev.springsecuritylesson.config.JwtUtil;
import com.lazarev.springsecuritylesson.config.TokenConfig;
import com.lazarev.springsecuritylesson.config.dto.UserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //handle JWT
        String accessToken = getToken(request);
        if(accessToken != null) {
            UserInfo userInfo = jwtUtil.validateAccessToken(accessToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userInfo.username(), null, userInfo.authorities()
            );
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(TokenConfig.HEADER);
        if(header == null || !header.startsWith(TokenConfig.TOKEN_PREFIX)) {
            return null;
        }

        return header.replace(TokenConfig.TOKEN_PREFIX, "");
    }
}
