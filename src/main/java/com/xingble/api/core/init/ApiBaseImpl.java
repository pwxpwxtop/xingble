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
package com.xingble.api.core.init;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * @description: 初始化相关的操作
 * @author: pwx
 * @data: 2023/2/18 19:35
 * @version: 1.0
 */
public interface ApiBaseImpl {

    void handleApiBaseMain(Method method);

    /**
     * 处理@ApiController注解
     */
    void handleApiController();

    /**
     * 设置ApiBase类的id
     * @param method
     */
    void handleApiMethods(Method method);

    void handleMethods();



    void handleRequest(RequestMapping mapping);

    void handlePost(PostMapping mapping);

    void handleGet(GetMapping mapping);

    /**
     * 处理ApiBase的路径
     * @param lastPath
     */
    void handleApiBasePath(String [] lastPath);


    /**
     * 处理ApiBaseLength的参数个数
     * @param length
     */
    void handleApiBaseLength(int length);

    void handleApiBaseType();



    /**
     * 设置接口method参数名称
     */
//    String handleApiBaseParamName(Parameter parameter);


//    ApiDataType handleApiBaseParamApiDataType(Parameter parameter);

//    /**
//     *
//     * @param parameter
//     */
//    void handleApiBaseParamAll(Parameter parameter);

    void handleApiBaseParamAllApiModel(Class<?> cls, String name);

    void handleApiValue(Method method);

//    void handleApiField();

    /**
     * 接口参数描述信息
     */
//    String  handleApiDescribe(Parameter parameter);

    /**
     *
     * 处理接口参数
     *
     */
//    void handleApiParameList(Class<?> cls );

//    void handleParame(List<List<ApiParame>> list, Parameter parameter);

    /**
     * 处理集合和array的数据类型
     * @param parameter
     * @return data
     */
//    List<ApiParame> handleData(Parameter parameter);
}
