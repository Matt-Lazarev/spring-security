package com.lazarev.springsecuritylesson.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final DaoUserDetailsService daoUserDetailsService;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers(HttpMethod.GET).authenticated()
                        .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
                )
                .formLogin(Customizer.withDefaults())
                .authenticationProvider(inMemoryAuthenticationProvider())
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(daoUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
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
