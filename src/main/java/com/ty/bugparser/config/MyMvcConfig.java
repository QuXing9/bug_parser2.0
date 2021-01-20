package com.ty.bugparser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/SuspiciousResults/list").setViewName("/SuspiciousResults/list");
        registry.addViewController("/User/user").setViewName("/User/user");
        registry.addViewController("/main.html").setViewName("dashboard");
        registry.addViewController("/main").setViewName("dashboard");
        registry.addViewController("/User/ModifyPasswordPage").setViewName("/User/ModifyPasswordPage");
        registry.addViewController("SuspiciousResults/QuerySuspiciousTestcaseByKeyword").setViewName("/Query/QuerySuspiciousTestcaseByKeyword");
        registry.addViewController("/ErrorType/list").setViewName("/ErrorType/list");


    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html", "/", "/user/login",
                        "/css/**", "/img/**", "/js/**");
    }

}
