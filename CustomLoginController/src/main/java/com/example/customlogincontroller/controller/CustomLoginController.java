package com.example.customlogincontroller.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomLoginController {

    @GetMapping("/customLogin")
    public String login() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(token);
        return "从自定义登录接口登陆成功";
    }

}
