package com.example.authorizationdb.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定义用户相关网址映射的Controller
 */
@Controller
public class UserController {

    @RequestMapping("/user")
    public String user(Model model){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", name);
        return "user/user";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", name);
        return "admin/admin";
    }
}
