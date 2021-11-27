package com.example.simpleexample.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定义用户相关网址映射的Controller
 */
@Controller
public class UserController {

    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("username", user.getUsername());
        return "user/user";
    }

    @RequestMapping("/admin")
    public String admin(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("username", user.getUsername());
        return "admin/admin";
    }
}
