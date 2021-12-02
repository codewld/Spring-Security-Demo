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
public class VerifyCodeFilter extends GenericFilterBean {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(req.getMethod()) && "/login".equals(req.getServletPath())) {
            try {
                String code = request.getParameter("verify-code");
                String trueCode = (String) req.getSession().getAttribute("verify-code");
                if (!StringUtils.hasLength(code))
                    throw new AuthenticationServiceException("Verification code cannot be empty!");
                if (!trueCode.equalsIgnoreCase(code)) {
                    throw new AuthenticationServiceException("Verification code mismatch!");
                }
            } catch (AuthenticationException e) {
                authenticationFailureHandler.onAuthenticationFailure(req, res, e);
                return;
            }
        }
        chain.doFilter(request, response);

    }
}
