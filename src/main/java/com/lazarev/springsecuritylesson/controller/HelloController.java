package com.lazarev.springsecuritylesson.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping
    public String getStartMessageGet(){
        return "Hello!";
    }

    @PostMapping
    public String getStartMessagePost(){
        return "Hello!";
    }
}
