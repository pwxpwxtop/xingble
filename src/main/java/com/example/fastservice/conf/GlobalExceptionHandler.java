package com.example.fastservice.conf;


import com.fastservice.r.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 全局异常拦截处理
 * @author: pwx
 * @data: 2022/9/24 22:58
 * @version: 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler
    public R Exception(Exception e) {
        e.printStackTrace();
        return R.err(e);
    }

    @ExceptionHandler(NullPointerException.class)
    public R NullPointerException(NullPointerException e) {
        e.printStackTrace();
        return R.err(e, "空指针异常");
    }
}
