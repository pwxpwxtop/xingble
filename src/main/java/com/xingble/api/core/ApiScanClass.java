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
import com.xingble.api.conf.ApiConf;
import com.xingble.api.core.init.ApiScanClassImpl;
import com.xingble.api.enums.ApiDataType;
import com.xingble.api.model.ApiParame;
import com.xingble.api.utils.ApiScanUtils;
import com.xingble.api.utils.ApiUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 扫描所有模型类
 * @author: pwx
 * @data: 2023/3/2 15:09
 * @version: 1.0
 */
public class ApiScanClass implements ApiScanClassImpl {

    public static Map<String, Object> objectMap = new HashMap<>();
    //配置类
    private ApiConf apiConf = null;

    //报名
    private String [] packs = null;

    private ApiParame apiParame = null;

    private final Map<String, Object> mapValue = ApiBaseService.mapValue;
    @Override
    public void readConf(ApiConf apiConf) {
        this.apiConf = apiConf;
        if (apiConf != null){
            this.packs = apiConf.getPacks();
        }

    }

    public void startClassList(){
        if (packs == null){
            return;
        }

        for (String pack : packs) {
            for (Class<?> aClass : ApiScanUtils.getClasses(pack)) {
                List<ApiParame> parameList = new ArrayList<>();
                handleParameter(aClass, parameList);
                objectMap.put(aClass.getTypeName(), parameList);
            }
        }
    }

    public void handleParameter(Class<?> cls, List<ApiParame> parameList){
        if (cls == null){
            return;
        }

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {//需要做类型判断下。
            Class<?> typeCls = fields[i].getType();
            apiParame = new ApiParame();
            boolean isExist = true;
            if (fields[i].isAnnotationPresent(ApiField.class)){
                ApiField apiField = fields[i].getDeclaredAnnotation(ApiField.class);
                apiParame.setDescribe(apiField.value());
                isExist = apiField.exist();
            }
            apiParame.setKey(fields[i].getName());
            if (ApiUtils.isString(typeCls) && isExist){
                apiParame.setApiType(ApiDataType.STRING);
            }else if (ApiUtils.isNumber(typeCls)){
                apiParame.setApiType(ApiDataType.NUMBER);
            }else if (ApiUtils.isBoolean(typeCls)){
                apiParame.setApiType(ApiDataType.BOOLEAN);
            } else if (ApiUtils.isMap(typeCls)) {
                apiParame.setApiType(ApiDataType.MAP);
                Type type = fields[i].getGenericType();
                if (!(type instanceof  ParameterizedType)){
                    return;
                }
                ParameterizedType genericType = (ParameterizedType) type;
                Type [] types = genericType.getActualTypeArguments();
                for (int i1 = 0; i1 < types.length; i1++) {
                    if (i1 == 1){
                       apiParame.setChildrenId(types[i1].getClass().getTypeName());
                    }
                }
            } else if (ApiUtils.isCollection(typeCls)){
                apiParame.setApiType(ApiDataType.COLLECTION);
                Type type = fields[i].getGenericType();
                if (!(type instanceof  ParameterizedType)){
                    return;
                }
                ParameterizedType genericType = (ParameterizedType) type;
                Type type1 = genericType.getActualTypeArguments()[0];
                apiParame.setChildrenId(type1.getClass().getTypeName());
            }else if (ApiUtils.isArray(typeCls)){
                apiParame.setApiType(ApiDataType.ARRAY);
                Class<?> aClass = fields[i].getType().getComponentType();
                apiParame.setChildrenId(aClass.getTypeName());

            }else if (ApiUtils.isFile(typeCls)){
                apiParame.setApiType(ApiDataType.FILE);
            }else if (ApiUtils.isModel(typeCls)){
                apiParame.setApiType(ApiDataType.MODEL);
                apiParame.setChildrenId(fields[i].getType().getTypeName());
            }else {
                apiParame.setApiType(ApiDataType.OTHER);
                apiParame.setChildrenId(fields[i].getType().getTypeName());
            }
            initDefaultValue(fields[i].getName());
            parameList.add(apiParame);

        }

        if (cls.getSuperclass() != null){
            handleParameter(cls.getSuperclass(), parameList);
        }
    }


    public void initDefaultValue(String name) {
        if (mapValue != null && apiParame != null) {
            Object value = mapValue.get(name);
            if (value != null) {
                apiParame.setVal(value);
            }
            value = mapValue.get(name + "*");
            if (value != null) {
                apiParame.setIsRequired(true);//必填项目
                apiParame.setVal(value);
            }else {
                apiParame.setIsRequired(false);
            }
        }else {
            if (apiParame != null){
                apiParame.setIsRequired(false);
            }
        }
    }

}
