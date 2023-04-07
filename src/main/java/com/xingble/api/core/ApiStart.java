/**
 * Copyright 2023 xingble PuWeiXiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xingble.api.core;

import com.xingble.api.conf.ApiConf;
import com.xingble.api.controller.FastGetController;
import com.xingble.api.model.ApiObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import java.util.*;

/**
 * @author pwx
 * @version 1.0
 * @describe
 * @date 2022/11/28 9:00
 */
public class ApiStart implements  ApplicationContextAware {

    public static List<ApiObject> list = new ArrayList<>();

    private ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext app) throws BeansException {
        this.applicationContext = app;
        for (String s : app.getBeanNamesForAnnotation(Controller.class)) {
            new ApiBaseService(app.getBean(s).getClass());
        }
        //启动解析业务扫描
        ApiListService apiListService = new ApiListService();
        apiListService.executeTree();
        //配置类
        ApiConf apiConf = new ApiConf();
        apiConf.setIp("127.0.0.1");
        apiConf.setPort("8080");
        apiConf.setPacks(new String[]{"com.example.fastservice.model"});
        //执行扫描包
        ApiScanClass scanClass = new ApiScanClass();
        scanClass.readConf(apiConf);
        scanClass.startClassList();

        ApiConfService conf = new ApiConfService(apiConf);
        conf.initConf();//初始化配置
        register();//注册容器
    }

    private void register(){
        registerBean(FastGetController.class, FastGetController.class.getSimpleName());
    }
    /**
     * 动态注入bean
     *
     * @param requiredType 注入类
     * @param beanName     bean名称
     */
    public void registerBean(Class<?> requiredType, String beanName) {

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

        //获取BeanFactory
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();

        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(requiredType);

        //动态注册bean.
        factory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

    }

}
