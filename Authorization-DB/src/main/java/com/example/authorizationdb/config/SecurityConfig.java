package com.example.authorizationdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MySecurityMetadataSource mySecurityMetadataSource;

    @Autowired
    MyAccessDecisionManager myAccessDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 在内存中创建一个名为 "user" 的用户，密码为 "pwd"，拥有 "USER" 权限
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("pwd")).roles("USER");
        // 在内存中创建一个名为 "admin" 的用户，密码为 "pwd"，拥有 "USER" 和"ADMIN"权限
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("pwd")).roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 首页、登录页、错误页，不需要权限即可访问
                .antMatchers("/", "/index", "/login", "/error").permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(mySecurityMetadataSource);
                        object.setAccessDecisionManager(myAccessDecisionManager);
                        return object;
                    }
                })
                .and()
                // 登录地址为 "/login"，登录成功默认跳转至 "/user"
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                .and()
                // 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }

}
