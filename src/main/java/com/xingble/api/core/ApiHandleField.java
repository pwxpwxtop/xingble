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


import com.xingble.api.animation.ApiField;
import com.xingble.api.core.init.ApiHandleFieldImpl;
import com.xingble.api.enums.ApiDataType;
import com.xingble.api.model.ApiParame;
import com.xingble.api.utils.ApiUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @description: 用来处理基础字段，包括String，StringBuffer，StringBuilder，八大基础类型和对应的引用类型(除了char类型)
 * @author: pwx
 * @data: 2023/2/27 16:56
 * @version: 1.0
 */
@Data
public class ApiHandleField implements ApiHandleFieldImpl {

    private ApiParame apiParame = null;

    private Map<String, Object> mapValue = null;

    public ApiHandleField(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }

    @Override
    public void handleField(Field field) {
        if (!field.isAnnotationPresent(ApiField.class)) {
            return;
        }
        ApiField apiField = field.getDeclaredAnnotation(ApiField.class);
        if (apiField.exist()){
            return;
        }
        apiParame = new ApiParame();
        apiParame.setDescribe(apiField.value());//设置描述和信息
        apiParame.setKey(field.getName());//设置参数名称
        initDefaultValue(field.getName());//初始化默认值和必填项目

        Class<?> type = field.getType();
        if (ApiUtils.isNumber( type )){
            handleNumber(field);
        }else if (ApiUtils.isString( type )){
            handleString(field);
        }else if (ApiUtils.isBoolean( type )){
            handleBoolean(field);
        }
    }

    @Override
    public void handleNumber(Field field) {
        apiParame.setApiType(ApiDataType.NUMBER);
    }

    @Override
    public void handleString(Field field) {
        apiParame.setApiType(ApiDataType.STRING);
    }

    @Override
    public void handleBoolean(Field field) {
        apiParame.setApiType(ApiDataType.BOOLEAN);
    }

    @Override
    public void initDefaultValue(String name) {
        if (mapValue != null) {
            Object value = mapValue.get(name);
            if (value != null) {
                apiParame.setVal(value);
            }
            value = mapValue.get(name + "*");
            if (value != null) {
                apiParame.setIsRequired(true);//必填项目
                apiParame.setVal(value);
            }
        }
    }
    /**
     * 1.将所有参数放到容器中,
     * 每一个对象都有一个id: uuid,或者雪花算法id，名称组合
     * 参数有：名称,数据类型,描述,默认值,是否为必填项目,
     * 组合起来
     *
     *
     *
     */
}
