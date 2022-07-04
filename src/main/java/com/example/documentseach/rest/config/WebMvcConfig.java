package com.example.documentseach.rest.config;

import com.example.documentseach.rest.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangpengkai
 */
@Configuration
@Profile("!test")
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**").excludePathPatterns("/hello");
    }
}
