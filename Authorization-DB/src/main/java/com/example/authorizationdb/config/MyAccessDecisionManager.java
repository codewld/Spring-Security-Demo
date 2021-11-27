package com.example.authorizationdb.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        // 获取当前用户具有的角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // configAttributes 由 SecurityMetadataSource 的 getAttributes() 获取，是资源允许访问的角色列表
        for (ConfigAttribute attribute : configAttributes) {
            String role = attribute.getAttribute();
            // 资源不需要角色，直接 return
            if ("ROLE_NONE".equals(role)) {
                return;
            }
            for (GrantedAuthority authority : authorities) {
                // 匹配成功，直接 return
                if (authority.getAuthority().equals(role)) {
                    return;
                }
            }
        }
        // 匹配失败
        throw new AccessDeniedException("你没有访问" + object + "的权限!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
