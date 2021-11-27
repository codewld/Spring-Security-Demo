package com.example.authorizationdb.config;

import com.example.authorizationdb.bean.Resource;
import com.example.authorizationdb.dao.ResourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;


@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    ResourceDao resourceDao;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取要访问的资源 URL
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 获取所有 Resource，遍历进行路径匹配，匹配成功则返回资源允许访问的角色列表
        for (Resource resource : resourceDao.findAll()) {
            if (antPathMatcher.match(resource.getUrl(), requestUrl) && resource.getRolesArray().length > 0) {
                return SecurityConfig.createList(resource.getRolesArray());
            }
        }
        //匹配不成功，则返回 ROLE_NONE，表示不需要角色即可访问
        return SecurityConfig.createList("ROLE_NONE");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
