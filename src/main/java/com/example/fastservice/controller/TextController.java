package com.example.fastservice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: pwx
 * @data: 2023/3/2 15:51
 * @version: 1.0
 */
@Controller
public class TextController {

    @RequestMapping
    public String index(){
        return "index.html";
    }
}
