package com.example.verifycode.controller;

import com.example.verifycode.util.VerifyCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class LoginController {
    @GetMapping("/verify-code")
    public void code(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerifyCode verifyCode = new VerifyCode();
        BufferedImage verifyCodeImage = verifyCode.getImage();
        String text = verifyCode.getText();
        HttpSession session = req.getSession();
        session.setAttribute("verify-code", text);
        VerifyCode.output(verifyCodeImage, resp.getOutputStream());
    }
}
