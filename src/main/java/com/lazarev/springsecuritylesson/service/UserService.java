package com.lazarev.springsecuritylesson.service;

import com.lazarev.springsecuritylesson.entity.ApplicationUser;
import com.lazarev.springsecuritylesson.entity.Role;
import com.lazarev.springsecuritylesson.repository.ApplicationUserRepository;
import com.lazarev.springsecuritylesson.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initDefaultUsers() {
        Role roleUser = new Role(); //id
        roleUser.setName("USER");

        Role roleAdmin = new Role(); //id
        roleAdmin.setName("ADMIN");

        roleRepository.saveAll(List.of(roleUser, roleAdmin)); //persistence context

        ApplicationUser user = new ApplicationUser();
        user.setUsername("Mike");
        user.setRoles(List.of(roleUser));
        user.setPassword(passwordEncoder.encode("12345"));

        ApplicationUser admin = new ApplicationUser();
        admin.setUsername("Bob");
        admin.setRoles(List.of(roleAdmin));
        admin.setPassword(passwordEncoder.encode("12345"));

        userRepository.saveAll(List.of(user, admin));
    }
}
