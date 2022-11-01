package com.example.springbootstudy.config;


import com.example.springbootstudy.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoderConfig passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoderConfig = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/","/registration","/activate/*","/img/**","/page/**","/static/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
       authenticationManagerBuilder.userDetailsService(customUserDetailsService)
               .passwordEncoder(passwordEncoderConfig.getPasswordEncoder());
    }

}
