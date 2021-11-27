package com.example.authenticationdb.config;

import com.example.authenticationdb.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // "/", "/index", "/error"  不需要权限即可访问
                .antMatchers("/", "/index", "/error").permitAll()
                // "/user" 及其以下所有路径，都需要 "USER" 权限
                .antMatchers("/user/**").hasRole("USER")
                // "/admin" 及其以下所有路径，都需要 "ADMIN" 权限
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                // 登录地址为 "/login"，登录成功默认跳转至 "/user"
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                .and()
                // 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
