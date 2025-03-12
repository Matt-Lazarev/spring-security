package com.lazarev.springsecuritylesson.config;

import com.lazarev.springsecuritylesson.entity.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {
    private final ApplicationUser user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of("ROLE_" + user.getRole()).map(SimpleGrantedAuthority::new).toList();
    }
}
