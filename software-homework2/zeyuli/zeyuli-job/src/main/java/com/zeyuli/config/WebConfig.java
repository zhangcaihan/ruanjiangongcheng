package com.zeyuli.config;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 交由前端进行重定向
 *
 * @author 李泽聿
 * @since 2025-12-12 20:33
 */

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将所有未匹配的路径转发到 /index.html，由 Vue Router 处理
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}