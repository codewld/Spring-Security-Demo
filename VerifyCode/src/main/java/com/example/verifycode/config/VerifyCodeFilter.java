package com.example.verifycode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VerifyCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {
            try {
                String code = request.getParameter("verify-code");
                String trueCode = (String) request.getSession().getAttribute("verify-code");
                if (!StringUtils.hasLength(code))
                    throw new AuthenticationServiceException("Verification code cannot be empty!");
                if (!trueCode.equalsIgnoreCase(code)) {
                    throw new AuthenticationServiceException("Verification code mismatch!");
                }
            } catch (AuthenticationException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // @Override
    // public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    //         throws IOException, ServletException {
    //     System.out.println("coming");
    //     HttpServletRequest request = (HttpServletRequest) req;
    //     HttpServletResponse response = (HttpServletResponse) res;
    //     System.out.println(request.getServletPath());
    //     if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {
    //         System.out.println("in");
    //         String code = request.getParameter("verify-code");
    //         String trueCode = (String) request.getSession().getAttribute("verify-code");
    //         System.out.println(code);
    //         System.out.println(trueCode);
    //         if (!StringUtils.hasLength(code))
    //             throw new AuthenticationServiceException("验证码不能为空!");
    //         if (!trueCode.equalsIgnoreCase(code)) {
    //             throw new AuthenticationServiceException("验证码错误!");
    //         }
    //     }
    //     chain.doFilter(request, response);
    // }
}
