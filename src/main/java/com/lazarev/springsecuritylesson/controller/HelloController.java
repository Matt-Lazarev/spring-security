package com.lazarev.springsecuritylesson.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getStartMessage(){
        return "Hello!";
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postStartMessage(){
        return "Hello!";
    }
}
