package com.lazarev.springsecuritylesson.config.dto;

public record AuthRequest(
        String username,
        String password
) { }
