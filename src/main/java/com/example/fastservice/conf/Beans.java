package com.example.fastservice.conf;


import com.fastservice.bean.FastServiceBeans;
import com.fastservice.bean.MapperBean;
import com.fastservice.dao.Dao;
import com.fastservice.query.Select;
import com.fastservice.query.Sql;
import com.xingble.api.core.ApiStart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: pwx
 * @data: 2022/11/17 22:26
 * @version: 1.0
 */
@Configuration
public class Beans {


    @Bean
    public MapperBean mapperBean(){
        return new MapperBean(Dao.class);
    }

    @Bean
    public Sql sql(){
        return new Sql();
    }

    @Bean
    public Select select(){
        return  new Select();
    }

    @Bean
    public FastServiceBeans serviceBeans(){
        return new FastServiceBeans();
    }

    @Bean
    public ApiStart apiStart(){
        return new ApiStart();
    }

}
