package com.example.springbootstudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Configuration
public class PasswordEncoder {

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
