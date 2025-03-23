package com.lazarev.springsecuritylesson;

import com.lazarev.springsecuritylesson.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityLessonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityLessonApplication.class, args);
    }

    @Bean
    CommandLineRunner clr(UserService userService) {
        return args -> userService.initDefaultUsers();
    }

}
