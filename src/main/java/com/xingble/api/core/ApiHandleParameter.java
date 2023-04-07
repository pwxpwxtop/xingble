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


import com.xingble.api.animation.ApiDescribe;
import com.xingble.api.animation.ApiField;
import com.xingble.api.core.init.ApiHandleParameterImpl;
import com.xingble.api.enums.ApiContentType;
import com.xingble.api.enums.ApiDataType;
import com.xingble.api.model.ApiBase;
import com.xingble.api.model.ApiParame;
import com.xingble.api.utils.ApiUtils;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.*;
import java.util.*;

/**
 * @description: 用来处理基础字段，包括String，StringBuffer，StringBuilder，八大基础类型和对应的引用类型(除了char类型)
 * @author: pwx
 * @data: 2023/2/27 16:56
 * @version: 1.0
 */
@Data
public class ApiHandleParameter implements ApiHandleParameterImpl {


    private Map<String, ApiParame> parameMap = new HashMap<>();

    private List<ApiParame> parameList = new ArrayList<>();

    private ApiBase apiBase = null;
    //参数必填项目
    private Map<String, Object> mapValue = null;

    private ApiParame apiParame = null;

    private String controllerName = null;

    private static int ID = 0;//分配id

    public ApiHandleParameter(ApiBase apiBase, Method method, String controllerName) {
        this.apiBase = apiBase;
        this.mapValue = apiBase.getParamValue();
        this.controllerName = controllerName;
        startHandle(method);
    }


    public void startHandle(Method method){

        Parameter [] parameters = method.getParameters();//方法上的参数设置
        for (int i = 0; i < parameters.length; i++) {
            apiParame = new ApiParame();
            String parameName = parameters[i].getName();
            apiParame.setKey(parameName);//设置名称
            StringJoiner joiner = new StringJoiner(":");
            joiner.add(controllerName).add(method.getName()).add(parameters[i].getName());
            apiParame.setId(joiner.toString());//设置唯一id
            if (parameters[i].isAnnotationPresent(ApiDescribe.class)) {//设置描述信息
                ApiDescribe apiField = parameters[i].getDeclaredAnnotation(ApiDescribe.class);
                apiParame.setDescribe(apiField.value());
            }
            selectParameterType(parameters[i]);//选择参数类型
            initDefaultValue(parameName);//初始化参数设置(默认值以及必填项目)
            parameMap.put(parameName, apiParame);
            parameList.add(apiParame);
        }

        apiBase.setParameters(parameList);
    }



    public void  selectParameterType(Parameter parameter){
        Class<?> cls = parameter.getType();
        handAnnotate(parameter);//设置参数类型
        if (ApiUtils.isModel(cls)){//实体类类型
            handleModel(parameter);
        }else if (ApiUtils.isFiles(cls)){//文件类型
            handleFile(parameter, ApiContentType.FILES);
        }else if (ApiUtils.isFile(cls)){//文件类型
            handleFile(parameter, ApiContentType.FILE);
        }else if (ApiUtils.isFile(parameter)){//文件类型
            handleFile(parameter, ApiContentType.FILE);
        }else if (ApiUtils.isBaseString(cls)){//基本数据类型
            handleBase(parameter);
        }else if (ApiUtils.isArray(cls)){//list集合类型
            handleArray(parameter);
        }else if (ApiUtils.isCollection(cls)){
            handleCollection(parameter);
        } else if (ApiUtils.isMap(cls)){//map集合类型
            handleMap(parameter);
        }
    }

    /**
     * 处理参数上的注解
     * @param parameter
     */
    public void  handAnnotate(Parameter parameter){
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            apiParame.setApiContentType(ApiContentType.BODY);
        }else if(parameter.isAnnotationPresent(RequestParam.class)){//参数类型
            RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
            if (!"".equals(requestParam.value().trim())){
                apiParame.setKey(requestParam.value().trim());
            }
            apiParame.setApiContentType(ApiContentType.PARAME);
            apiParame.setIsRequired(true);
        }else if (parameter.isAnnotationPresent(PathVariable.class)){
            apiParame.setApiContentType(ApiContentType.PATH);
        }else {
            apiParame.setApiContentType(ApiContentType.NONE);
        }
    }

    public  void handleModel(Parameter parameter){
        Class<?> cls = parameter.getType();
//        apiParame.setChildrenId(cls.getTypeName());
        apiParame.setApiType(ApiDataType.OBJECT);
        apiParame.setParentType(ApiDataType.OBJECT);
        apiParame.setChildren(handleModel(cls, ApiDataType.OBJECT));
    }

    //控制countMap的大小
    private Map<String, Integer> countMap = new HashMap<>();

    public List<ApiParame> handleModel(Class<?> cls, ApiDataType pT){
        List<ApiParame> parameList = new ArrayList<>();
        Field [] fields = cls.getDeclaredFields();
        ApiParame apiParame = null;
        for (Field field : fields) {
            boolean isExist = true;
            ApiField apiField = null;
            if (field.isAnnotationPresent(ApiField.class)) {
                apiField = field.getDeclaredAnnotation(ApiField.class);
                isExist = apiField.exist();
            }
            if (!isExist){
                return parameList;
            }
            Class<?> clsT = field.getType();
//            String id = pN + ":" + field.getName();
//            String id = ApiId.getIds();
            String id = "uid:" + ID++;
            if (ApiUtils.isBaseString(clsT)){//基础数据类型
                apiParame = handleBase(field, pT);
            }else if (ApiUtils.isModel(clsT)){//实体类类型
                apiParame = new ApiParame();
                apiParame.setKey("entity");
                apiParame.setParentType(pT);
                apiParame.setApiType(ApiDataType.OBJECT);
                Integer count = countMap.get(cls.getTypeName());
                List<ApiParame> apiParameList = null;
                if (count == null){
                    countMap.put(cls.getTypeName(), 0);
                    apiParameList =  handleModel(clsT, ApiDataType.OBJECT);
                }else {
                    apiParame = null;
                }
                if (apiParameList != null){
                    apiParame.setChildren(apiParameList);
                }
            }else if (ApiUtils.isArray(clsT)){//是否为数组
                apiParame = new ApiParame();
                apiParame.setApiType(ApiDataType.ARRAY);
                List<ApiParame> apiParameList = handleArray(clsT, ApiDataType.ARRAY);
                apiParame.setChildren(apiParameList);
            }else if (ApiUtils.isCollection(clsT)){//是否为集合
                apiParame = new ApiParame();
                apiParame.setApiType(ApiDataType.ARRAY);
                List<ApiParame> apiParameList = handleList(field, id);
                apiParame.setChildren(apiParameList);
            }else {
                apiParame = null;
            }
            if (apiParame != null){
                initDefaultValue(field.getName(), apiParame);
                apiParame.setId(id);
                apiParame.setKey(field.getName());
                apiParame.setParentType(pT);
                parameList.add(apiParame);
                if (apiField != null){
                    apiParame.setDescribe(apiField.value());
                }
            }
        }
        if (parameList.size() != 0){
            return parameList;
        }
        return null;
    }

    /**
     * 处理集合类型的数据
     * @param field
     * @param pN
     * @return
     */
    public List<ApiParame> handleList(Field field, String pN){
        List<ApiParame> parameList = null;
        Type type = field.getGenericType();
//        String id = pN + ":" + field.getName();
//        String id = ApiId.getIds();
        String id = "uid:" + ID++;
        if (type instanceof ParameterizedType) {//泛型
            parameList = handleType(((ParameterizedType) type).getActualTypeArguments()[0], ApiDataType.ARRAY);
        }
        return parameList;
    }

    public List<ApiParame> handleType(Type type, ApiDataType pT){
        List<ApiParame> parameList = new ArrayList<>();
        ApiParame apiParame = null;

//        String id = ApiId.getIds();
        String id = "uid:" + ID++;
        if (type instanceof ParameterizedType){
            Type [] types1 = ((ParameterizedType) type).getActualTypeArguments();
            for (int i = 0; i < types1.length; i++) {
                if (types1.length == 1){//处理泛型个数未 List<String> 为1的
                    apiParame = new ApiParame();
                    apiParame.setApiType(ApiDataType.ARRAY);
                    List<ApiParame> apiParames = handleType(types1[i] , ApiDataType.ARRAY);
                    apiParame.setChildren(apiParames);
                }else if (types1.length == 2 && i == 1){
                    apiParame = new ApiParame();
                    apiParame.setKey("obj");
                    apiParame.setApiType(ApiDataType.OBJECT);
                    List<ApiParame> apiParames = handleType(types1[i] , ApiDataType.OBJECT);
                    apiParame.setChildren(apiParames);
                }
            }
        }else {
            if (type instanceof Class){
                Class<?> cls = (Class<?>) type;
                if (ApiUtils.isModel(cls)){
                    apiParame = new ApiParame();
                    apiParame.setKey("entity");
                    apiParame.setParentType(pT);
                    apiParame.setApiType(ApiDataType.OBJECT);
                    Integer count = countMap.get(cls.getTypeName());
                    List<ApiParame> apiParameList = null;
                    if (count == null){
                        countMap.put(cls.getTypeName(), 0);
                        apiParameList =  handleModel(cls, ApiDataType.OBJECT);
                    }else {
                        apiParame = null;
                    }
                    if (apiParameList != null){
                        apiParame.setChildren(apiParameList);
                    }
                }else if (ApiUtils.isBaseString(cls)){
                    apiParame = handleBase(cls, pT);
                } else if (ApiUtils.isArray(cls)) {
                    Integer count = countMap.get(cls.getTypeName());
                    List<ApiParame> apiParameList = null;
                    if (count == null){
                        countMap.put(cls.getTypeName(), 0);
                        apiParame = new ApiParame();
                        apiParame.setApiType(ApiDataType.ARRAY);
                        apiParameList =  handleArray(cls, ApiDataType.ARRAY);
                    }else {
                        apiParame = null;
                    }
                    if (apiParameList != null){
                        apiParame.setChildren(apiParameList);
                    }

                }
            }
        }
        if (apiParame != null){
            apiParame.setId(id);
            parameList.add(apiParame);
        }
        return parameList;
    }

    /**
     * 处理泛型的数据类型
     * @param types
     * @param pT
     * @return
     */
    public List<ApiParame> handleType(Type [] types, ApiDataType pT){
        List<ApiParame> parameList = new ArrayList<>();
        ApiParame apiParame = null;
        int i = 0;
        for (Type type : types) {
//            String id = pN + ":" + type.getTypeName();
//            String id = ApiId.getIds();
            String id = "uid:" + ID++;
            if (type instanceof ParameterizedType){
                Type [] types1 = ((ParameterizedType) type).getActualTypeArguments();
                if (types1.length == 1){//处理泛型个数未 List<String> 为1的
                    apiParame = new ApiParame();
                    apiParame.setApiType(ApiDataType.ARRAY);
                    apiParame.setParentType(pT);
                    List<ApiParame> apiParames = handleType(types1 ,ApiDataType.ARRAY);

                    apiParame.setChildren(apiParames);
                }else if (types1.length == 2 && i == 1){
                    apiParame = new ApiParame();
                    apiParame.setApiType(ApiDataType.OBJECT);
                    apiParame.setParentType(pT);
                    List<ApiParame> apiParames = handleType(types1 , ApiDataType.OBJECT);
                    apiParame.setChildren(apiParames);
                }
            }else {
                if (type instanceof Class){
                    Class<?> cls = (Class<?>) type;
                    if (ApiUtils.isModel(cls)){
                        apiParame = new ApiParame();
                        apiParame.setKey("entity");
                        apiParame.setApiType(ApiDataType.OBJECT);
                        Integer count = countMap.get(cls.getTypeName());
                        List<ApiParame> apiParameList = null;
                        if (count == null){
                            countMap.put(cls.getTypeName(), 0);
                            apiParameList =  handleModel(cls, ApiDataType.OBJECT);
                        }else {
                            apiParame = null;
                        }
                        if (apiParameList != null){
                            apiParame.setChildren(apiParameList);
                        }
                    }else if (ApiUtils.isBaseString(cls)){
                        apiParame = handleBase(cls, pT);
                    } else if (ApiUtils.isArray(cls)) {
                        Integer count = countMap.get(cls.getTypeName());
                        List<ApiParame> apiParameList = null;
                        if (count == null){
                            countMap.put(cls.getTypeName(), 0);
                            apiParame = new ApiParame();
                            apiParame.setApiType(ApiDataType.ARRAY);
                            apiParameList =  handleArray(cls, ApiDataType.ARRAY);
                        }else {
                            apiParame = null;
                        }
                        if (apiParameList != null){
                            apiParame.setParentType(pT);
                            apiParame.setChildren(apiParameList);
                        }

                    }
                }
            }
            if (apiParame != null){
                apiParame.setId(id);
                parameList.add(apiParame);
            }
            i++;
        }
        return parameList;
    }


    public List<ApiParame> handleArray(Class<?> cls, ApiDataType pT){
        List<ApiParame> apiParameList = new ArrayList<>();
        ApiParame apiParame = null;
        Class<?> clsArr = cls.getComponentType();
//        String id = pN + ":" + clsArr.getSimpleName();
//        String id = ApiId.getIds();
        String id = "uid:" + ID++;
        if (ApiUtils.isArray(clsArr)){//判断是否为数组类型
            Integer count = countMap.get(cls.getTypeName());
            if (count == null){
                countMap.put(cls.getTypeName() , 0);
                apiParame = new ApiParame();
                apiParame.setParentType(pT);
                apiParame.setApiType(ApiDataType.ARRAY);
                List<ApiParame> apiParames = handleArray(clsArr, ApiDataType.ARRAY);
                apiParame.setChildren(apiParames);
            }
        }else if (ApiUtils.isBaseString(clsArr)){//判断是否为基础类型
            apiParame = new ApiParame();
            if (ApiUtils.isString(clsArr)){
                apiParame.setApiType(ApiDataType.STRING);
            }else if (ApiUtils.isNumber(clsArr)){
                apiParame.setApiType(ApiDataType.NUMBER);
            }else if (ApiUtils.isBoolean(clsArr)){
                apiParame.setApiType(ApiDataType.BOOLEAN);
            }else {
                apiParame.setApiType(ApiDataType.OTHER);
            }
        }else if (ApiUtils.isModel(clsArr)){
            Integer count = countMap.get(cls.getTypeName());
            if (count == null) {
                countMap.put(cls.getTypeName(), 0);
                apiParameList = handleModel(clsArr, ApiDataType.OBJECT);
            }
        }else {
            return null;
        }

        if (apiParame != null){
            apiParame.setId(id);
            apiParame.setParentType(pT);
            apiParameList.add(apiParame);
        }
        return apiParameList;
    }


    public void handleBase(Parameter parameter){
        Class<?> cls = parameter.getType();
        apiParame = handleBase(cls, ApiDataType.OBJECT);

    }

    public ApiParame handleBase(Object obj, ApiDataType pT){
        ApiParame apiParame = new ApiParame();
        apiParame.setParentType(pT);
        if (obj instanceof Field){
            Field field = (Field) obj;
            Class<?> cls = field.getType();
            apiParame.setKey(field.getName());
            boolean isExist = false;
            if (field.isAnnotationPresent(ApiField.class)){
                ApiField apiField = field.getDeclaredAnnotation(ApiField.class);
                apiParame.setDescribe(apiField.value());
                isExist = apiField.exist();
            }
            if (!isExist){
                return null;
            }

            if (ApiUtils.isString(cls)){
                apiParame.setApiType(ApiDataType.STRING);
            }else if (ApiUtils.isNumber(cls)){
                apiParame.setApiType(ApiDataType.NUMBER);
            }else if (ApiUtils.isBoolean(cls)){
                apiParame.setApiType(ApiDataType.BOOLEAN);
            }else {
                apiParame.setApiType(ApiDataType.OTHER);
            }
            initDefaultValue(field.getName(), apiParame);
            return apiParame;
        }else if (obj instanceof Class){
            Class<?> cls = (Class<?>) obj;
            if (ApiUtils.isString(cls)){
                apiParame.setApiType(ApiDataType.STRING);
            }else if (ApiUtils.isNumber(cls)){
                apiParame.setApiType(ApiDataType.NUMBER);
            }else if (ApiUtils.isBoolean(cls)){
                apiParame.setApiType(ApiDataType.BOOLEAN);
            }else {
                apiParame.setApiType(ApiDataType.OTHER);
            }
        }
        return apiParame;
    }

    public  void handleCollection(Parameter parameter){
        Object obj = recurrenceArray(parameter , -1);
        Type type = parameter.getParameterizedType();
        if (type instanceof ParameterizedType){
             List<ApiParame>  apiParameList =  handleType(((ParameterizedType) type).getActualTypeArguments(), ApiDataType.ARRAY );
             apiParame.setChildren(apiParameList);
        }
        apiParame.setParentType(ApiDataType.OBJECT);
        apiParame.setKey(parameter.getName());
        apiParame.setStructure(obj);//参数结构
        apiParame.setApiType(ApiDataType.ARRAY);//数据类型
    }
    /**
     * 处理list和array类型
     * @param parameter
     */
    public  void handleArray(Parameter parameter){
        Object obj = recurrenceArray(parameter , -1);
        apiParame.setStructure(obj);//参数结构
        List<ApiParame> parameList =  handleArray(parameter.getType(), ApiDataType.ARRAY);
        apiParame.setApiType(ApiDataType.ARRAY);//数据类型
        apiParame.setParentType(ApiDataType.OBJECT);
        apiParame.setChildren(parameList);
    }

    public  void handleMap(Parameter parameter){
        Object obj = recurrenceArray(parameter , -1);
        Type type = parameter.getParameterizedType();
        if (type instanceof ParameterizedType){
            List<ApiParame>  apiParameList =  handleType(((ParameterizedType) type).getActualTypeArguments()[1], ApiDataType.OBJECT);
            apiParame.setChildren(apiParameList);
        }
        apiParame.setKey(parameter.getName());
        apiParame.setStructure(obj);//参数结构
        apiParame.setParentType(ApiDataType.OBJECT);
        apiParame.setApiType(ApiDataType.OBJECT);//数据类型
    }

    /**
     *
     * @param obj obj
     * @param count 递归次数
     */
    public Object recurrenceArray(Object obj, int count){

        if (obj == null){
            return null;
        }
        if (-1 <= count){
           count++;
        }else {
           return null;
        }

        if (obj instanceof Parameter){
            Parameter parameter = (Parameter) obj;
            Class<?> cls = parameter.getType();
            Class<?> clsArray = cls.getComponentType();
            if (ApiUtils.isData(cls) && clsArray == null){
                Type type = parameter.getParameterizedType();//数组类型
                return recurrenceArray(type, count);
            }else{
                return recurrenceArray(cls, count);
            }
        }else if (obj instanceof Type){
            Type type = (Type) obj;
            if (!(type instanceof ParameterizedType)){
                Class<?> cls = (Class<?>)type;
                if (ApiUtils.isData(cls)){
                    List<Object> objects = new ArrayList<>();
                    objects.add(recurrenceArray( cls.getComponentType(), count));
                    return objects;
                }
//                apiParame.setChildrenId(type.getTypeName());

                return type.getTypeName();
            }
            ParameterizedType parameter =  (ParameterizedType)type;
            Class<?> clazz = (Class<?>) parameter.getRawType();
            if (ApiUtils.isData(clazz)){
                List<Object> objects = new ArrayList<>();
                objects.add(recurrenceArray( parameter.getActualTypeArguments()[0], count));
                return objects;
            }else if (ApiUtils.isMap(clazz)){
                Type [] types = parameter.getActualTypeArguments();
                for (int i = 0; i < types.length; i++) {
                    if (i == 1){
                        Map<String, Object> map = new HashMap<>();
                        Object ob = recurrenceArray(types[i], count);
                        map.put("___",  ob);
                        return map;
                    }
                }
            }
        }
        return null;
    }





    public  void handleFile(Parameter parameter, ApiContentType dataType){
        Class<?> cls = parameter.getType();
//        apiParame.setChildrenId(cls.getTypeName());
        apiParame.setApiType(ApiDataType.FILE);
        apiParame.setApiContentType(dataType);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put( parameter.getName() , cls.getTypeName());
        apiParame.setStructure(objectMap);
    }

    public void initDefaultValue(String name, ApiParame apiParame) {
        if (mapValue != null) {
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
            apiParame.setIsRequired(false);
        }
    }

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
            }else {
                apiParame.setIsRequired(false);
            }
        }else {
            apiParame.setIsRequired(false);
        }
    }









}
