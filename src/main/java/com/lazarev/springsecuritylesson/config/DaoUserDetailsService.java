package com.lazarev.springsecuritylesson.config;

import com.lazarev.springsecuritylesson.config.model.SecurityUser;
import com.lazarev.springsecuritylesson.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DaoUserDetailsService implements UserDetailsService  {
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findApplicationUserByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username '%s' not found".formatted(username))
                );
    }
}
