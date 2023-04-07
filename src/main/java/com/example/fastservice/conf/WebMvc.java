package com.example.fastservice.conf;


import com.example.stye.UserArgumentResolver;
import com.example.stye.UserHandlerMethodReturnValueHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: pwx
 * @data: 2022/11/12 22:45
 * @version: 1.0
 */
@Component
@Configuration
public class WebMvc implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new UserHandlerMethodReturnValueHandler());
        WebMvcConfigurer.super.addReturnValueHandlers(handlers);
    }
}
