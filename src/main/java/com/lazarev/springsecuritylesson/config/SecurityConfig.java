package com.lazarev.springsecuritylesson.config;

import com.lazarev.springsecuritylesson.config.filter.CustomAuthenticationFilter;
import com.lazarev.springsecuritylesson.config.filter.JwtTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DaoUserDetailsService daoUserDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http,
                                            AuthenticationManager authManager) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c.anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilter(new CustomAuthenticationFilter(authManager, jwtUtil))
                .addFilterAfter(new JwtTokenVerifier(jwtUtil), CustomAuthenticationFilter.class)
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // 1. BCrypt -> hash:  12345 -> 12345.hash() -> 456235
    //                              12345.hash() -> 456235
    //                              54321.hash() -> 456235 (collision)
    //                              54344.hash() -> 514364

    // 2. HS256 -> encode + key ->  12345.encode(key) -> 456235

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(daoUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    public DaoAuthenticationProvider inMemoryAuthenticationProvider() {
        DaoAuthenticationProvider inMemoryAuthenticationProvider = new DaoAuthenticationProvider();
        inMemoryAuthenticationProvider.setUserDetailsService(inMemoryUserDetailsService());
        inMemoryAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return inMemoryAuthenticationProvider;
    }

    public InMemoryUserDetailsManager inMemoryUserDetailsService() {
        UserDetails user = User
                .withUsername("Kate")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
