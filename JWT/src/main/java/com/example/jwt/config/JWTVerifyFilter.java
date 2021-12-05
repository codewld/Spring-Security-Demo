package com.example.jwt.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTVerifyFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String jwtToken = req.getHeader("authorization");
        try {
            DecodedJWT decode = JWT.require(Algorithm.HMAC256("111111")).build().verify(jwtToken);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    decode.getAudience().get(0),
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(decode.getClaim("authorities").asString())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (JWTVerificationException e) {
            return;
        }
        filterChain.doFilter(request, response);
    }
}
