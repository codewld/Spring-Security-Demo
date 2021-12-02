package com.example.verifycode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    VerifyCodeFilter verifyCodeFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 在内存中创建一个名为 "user" 的用户，密码为 "pwd"，拥有 "USER" 权限
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("pwd")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                .and()
                .csrf().disable()
                .cors();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/verify-code");
    }
}
