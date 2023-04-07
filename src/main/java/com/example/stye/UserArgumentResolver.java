package com.example.stye;


import com.example.fastservice.model.Dep;
import com.fastservice.animation.ApiFilter;
import com.fastservice.exception.BodyException;
import com.fastservice.model.Params;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.BindException;

//实现HandlerMethodArgumentResolver接口来自定义一个参数处理器
public class UserArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(ApiFilter.class);
    }

    @Override
    public Object resolveArgument( MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        Parameter parameter1  = parameter.getParameter();
//        System.out.println(parameter1.getName());
//        System.out.println(parameter1.getType());
//        Type type = parameter1.getParameterizedType();
//        Dep dep = new Dep();
//        dep.setName("李白");
//        Class<?> typeClass =  parameter1.getType();
//        for (Field declaredField : typeClass.getDeclaredFields()) {
//            declaredField.setAccessible(true);
//            System.out.println(declaredField);
//            System.out.println(declaredField.get(dep));
//        }

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);



        return null;
    }
}

