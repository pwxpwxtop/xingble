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


import com.xingble.api.animation.*;
import com.xingble.api.core.init.ApiBaseImpl;
import com.xingble.api.enums.ApiBaseType;
import com.xingble.api.enums.ApiRequestType;
import com.xingble.api.model.ApiBase;
import com.xingble.api.utils.ApiUtils;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.*;
import java.util.*;

/**
 * @description:
 * @author: pwx
 * @data: 2023/2/18 19:35
 * @version: 1.0
 */
@Data
public class ApiBaseService implements ApiBaseImpl {

    private ApiBase apiBase;//单个接口

    public static List<ApiBase> apiBases = new ArrayList<>();//所有的接口

    private Class<?> cls;//控制层类

    private String id;//接口id

    private String [] paths;//RequestMapping路径

    private Method [] methods;//方法设置



    private Set<String> paramAll;

    //类名称
    private String controllerName;

    public static Map<String, Object> mapValue;

    public ApiBaseService(Class<?> aClass){
        if (aClass.isAnnotationPresent(ApiController.class)){
            init(aClass);
        }
    }


    private void init(Class<?> aClass){
        this.cls = aClass;
        this.methods = aClass.getDeclaredMethods();
        Arrays.sort(methods,Comparator.comparing(Method::getName));
        this.controllerName = aClass.getSimpleName();
        handleController();
        handleMethods();
    }

    /**
     * 处理所用控制接口
     */
    private void handleController(){
        RequestMapping mapping = null;
        if (cls.isAnnotationPresent(RequestMapping.class)){//初始化Controller路径
            mapping = cls.getDeclaredAnnotation(RequestMapping.class);
            if (mapping.value().length != 0) {
                paths = mapping.value();
            }else if (mapping.path().length != 0){
                paths = mapping.path();
            }else {
                paths = new String[]{"/"};
            }
        }else {
            paths = new String[]{"/"};
        }
        handleApiController();//初始化接口层
    }

    @Override
    public void handleApiController() {
        apiBase = new ApiBase();
        id = cls.getSimpleName();//默认id
        String parentId = null;
        String describe = null;
        if (cls.isAnnotationPresent(ApiController.class)){//包含apiController就加入到集合中
            ApiController controller = cls.getDeclaredAnnotation(ApiController.class);

            if (!"".equals(controller.id().trim())){
                id = controller.id().trim();//设置id标识符
            }

            if ("".equals(controller.parentId().trim())){
                parentId = "ROOT";//当没有使用ApiController接口的时候,则标识放在
            }else {
                parentId = controller.parentId();
            }

            if (!"".equals(controller.value())){
                describe = controller.value();//设置描述信息
            }
        }
        apiBase.setId(id);
        if (parentId == null){
            apiBase.setParentId("RIGHT");
        }else {
            apiBase.setParentId(parentId);
        }

        if (describe == null){
            apiBase.setDescribe(cls.getSimpleName());
        }else {
            apiBase.setDescribe(describe);
        }
        apiBase.setBaseType(ApiBaseType.FOLDER);//设置文件夹类型

        apiBases.add(apiBase);
    }




    @Override
    public void handleMethods() {
        for (Method method : methods) {
            apiBase = new ApiBase();
            if (method.isAnnotationPresent(RequestMapping.class)){
                handleRequest(method.getDeclaredAnnotation(RequestMapping.class));
                handleApiBaseMain(method);
            }else if (method.isAnnotationPresent(PostMapping.class)){
                handlePost(method.getDeclaredAnnotation(PostMapping.class));
                handleApiBaseMain(method);
            }else if (method.isAnnotationPresent(GetMapping.class)){
                handleGet(method.getDeclaredAnnotation(GetMapping.class));
                handleApiBaseMain(method);
            }

            if (!apiBase.isEmpty()){
                apiBases.add(apiBase);
            }

        }
    }

    @Override
    public void handleApiBaseMain(Method method) {
        handleApiMethods(method);
        handleApiBaseLength(method.getParameters().length);//设置参数长度
        handleApiBaseType();
        handleApiValue(method);//处理ApiValue注解
        new ApiHandleParameter( apiBase, method, controllerName);

//        handleApiBaseParameterMain(method.getParameters());//处理parameter类
    }



    @Override
    public void handleRequest(RequestMapping mapping) {
        String [] paths = null;
        if (mapping.value().length != 0) {
            paths = mapping.value();
        }else if (mapping.path().length != 0){
            paths = mapping.path();
        }else {
            paths = new String[]{"/"};
        }
        apiBase.setRequestType(ApiRequestType.ALL);
        handleApiBasePath(paths);
    }

    @Override
    public void handlePost(PostMapping mapping) {
        String [] paths = null;
        if (mapping.value().length != 0) {
            paths = mapping.value();
        }else if (mapping.path().length != 0){
            paths = mapping.path();
        }else {
            paths = new String[]{"/"};
        }
        apiBase.setRequestType(ApiRequestType.POST);
        handleApiBasePath(paths);
    }

    @Override
    public void handleGet(GetMapping mapping) {
        String [] paths = null;
        if (mapping.value().length != 0) {
            paths = mapping.value();
        }else if (mapping.path().length != 0){
            paths = mapping.path();
        }else {
            paths = new String[]{"/"};
        }
        apiBase.setRequestType(ApiRequestType.GET);
        handleApiBasePath(paths);
    }


    @Override
    public void handleApiBasePath(String []  lastPath) {
        List<String> apiPath = new ArrayList<>();
        for (String path : paths) {
            for (String s : lastPath) {
                apiPath.add(ApiUtils.pathCheck(path) + ApiUtils.pathCheck(s));
            }
        }
        apiBase.setPath(apiPath);
    }

    @Override
    public void handleApiBaseLength(int length) {
        apiBase.setLength(length);
    }

    @Override
    public void handleApiMethods(Method method){
        String id = method.getName();
        String parentId = this.id;
        String disable = method.getName();
        if (method.isAnnotationPresent(ApiMethod.class)) {
            ApiMethod apiMethod = method.getDeclaredAnnotation(ApiMethod.class);
            parentId = apiMethod.parentId().trim();
            if ("".equals(parentId)){
                parentId = this.id;
            }

            if (!"".equals(apiMethod.value())){
                disable = apiMethod.value();
            }

        }
        apiBase.setId(cls.getSimpleName() + ":" + id + ":" + method.getParameters().length);//设置id
        apiBase.setParentId(parentId);//设置父级id
        apiBase.setDescribe(disable);//设置备注
    }

//    @Override
//    public void handleApiBaseParameterMain(Parameter[] parameters) {
//        List<String> paramNames = new ArrayList<>();
//        List<ApiDataType> paramApiDataTypes = new ArrayList<>();
//        Map<String, String> paramDescribe = new HashMap<>();
//        List<List<ApiParame>> arrayList = new ArrayList<>();
//        paramAll = new HashSet<>();
//        for (Parameter parameter : parameters) {
//            paramNames.add(handleApiBaseParamName(parameter));//接口参数名称
//            paramApiDataTypes.add(handleApiBaseParamApiDataType(parameter));//接口参数类型
////            handleApiBaseParamAll(parameter);
//            paramDescribe.put( parameter.getName(), handleApiDescribe(parameter));//设置接口参数描述
//
//            handleParame(arrayList, parameter);
//        }
//
//        apiBase.setParamName(paramNames);//设置参数名称
//        apiBase.setParamApiDataType(paramApiDataTypes);
//        apiBase.setParamDescribe(paramDescribe);
//        apiBase.setParamAll(paramAll);
//        apiBase.setParameList(arrayList);
//        isExistMap = null;//重置
//    }

//    @Override
//    public String handleApiBaseParamName(Parameter parameter) {
//        return parameter.getName();
//    }

//    @Override
//    public ApiDataType handleApiBaseParamApiDataType(Parameter parameter) {
//        Class<?> cls = parameter.getType();
//        if (ApiUtils.isBaseString(cls)){
//            return ApiDataType.BASE;
//        }else if (ApiUtils.isModel(cls)){
//            return ApiDataType.MODEL;
//        }else if (ApiUtils.isFile(cls)){
//            return ApiDataType.FILE;
//        }else if (ApiUtils.isCollection(cls)){
//            return ApiDataType.COLLECTION;
//        }else if (ApiUtils.isMap(cls)){
//            return ApiDataType.MAP;
//        }else {
//            return ApiDataType.OTHER;
//        }
//    }

//    @Override
//    public void handleApiBaseParamAll(Parameter parameter) {
//        if (ApiUtils.isModel(parameter.getType())){
//            handleApiBaseParamAllApiModel(parameter.getType(), null);
//        }else if (ApiUtils.isCollection(parameter.getType())){
//
//        }else if (ApiUtils.isMap(parameter.getType())){
//
//        }
//
//    }

    @Override
    public void handleApiBaseParamAllApiModel(Class<?> cls, String name) {
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {//需要做类型判断下。
            Class<?> typeCls = fields[i].getType();
            if (ApiUtils.isBaseString(typeCls)){
                paramAll.add(fields[i].getName());
            }else if (ApiUtils.isModel(typeCls)){
                if (cls.getSuperclass() != null && !fields[i].getName().equals(name)){
                    handleApiBaseParamAllApiModel(fields[i].getType() , fields[i].getName());
                }
            }
        }

        if (cls.getSuperclass() != null){
            handleApiBaseParamAllApiModel(cls.getSuperclass(), null);
        }
    }

    @Override
    public void handleApiBaseType() {
        apiBase.setBaseType(ApiBaseType.FILE);
    }


    @Override
    public void handleApiValue(Method method) {
        Map<String, Object> map = null;
        if (method.isAnnotationPresent(ApiValue.class)) {
            ApiValue apiValue = method.getDeclaredAnnotation(ApiValue.class);
            mapValue = map = analysisApiValue(apiValue.value());
        }
        apiBase.setParamValue(map);
    }

    /**
     *  解析@ApiValue注解
     * @param str
     * @return
     */
    private static Map<String, Object> analysisApiValue(String str){
        if (str == null){
            return null;
        }
        String [] values = str.split("&");
        Map<String, Object> map = new HashMap<>();
        for (String value : values) {
            if (value.contains("=")){
                String [] keyVal = value.split("=");
                if (keyVal.length == 2){
                    if ("null".equals(keyVal[1].trim())){
                        map.put(keyVal[0].trim(), null);
                    }else {
                        map.put(keyVal[0].trim(), keyVal[1].trim());
                    }
                }
            } else if (value.contains("*")) {
                String [] keyVal = value.split("\\*");
                if (keyVal.length == 2){
                    if ("null".equals(keyVal[1].trim())){
                        map.put(keyVal[0].trim() + "*", null);
                    }else {
                        map.put(keyVal[0].trim() + "*", keyVal[1].trim());
                    }
                }
            }

        }

        if (map.size() == 0){
            return null;
        }else {
            return map;
        }
    }


//    @Override
//    public String handleApiDescribe(Parameter parameter) {
//        if (parameter.isAnnotationPresent(ApiDescribe.class)) {
//            ApiDescribe apiField = parameter.getDeclaredAnnotation(ApiDescribe.class);
//            return apiField.value();
//        }else {
//            return null;
//        }
//    }

//    @Override
//    public void handleApiParameList(Class<?> cls) {
//        ApiParame apiParame = new ApiParame();
//        if (ApiUtils.isModel(cls)){
//
//
//        }else if (ApiUtils.isBaseString(cls)){
//
//        }
//
//
//    }



//    @Override
//    public void handleParame(List<List<ApiParame>> list, Parameter parameter) {
//        List<ApiParame> parameList = null;
//        Class<?> cls = parameter.getType();
//        if (ApiUtils.isBaseString(cls)){//判断是否为基础类型
//            parameList = handleBase( parameter);
//        }else if (ApiUtils.isModel(parameter.getType())){//判断是否为实体类型
//            parameList = handleModel(cls);
//        }else if (ApiUtils.isData(cls)){//判断是否为list集合类型
//            parameList = handleData(parameter);
//        }
//        this.isRequired = false;
////        parameter.getType().getGenericSuperclass()
//        if (parameList != null) {
//            list.add(parameList);
//        }
//    }


//    public List<ApiParame> handleData(Parameter parameter) {
//        List<ApiParame> apiParameList = new ArrayList<>();
//        ApiParame array = new ApiParame();
//        array.setApiType(ApiDataType.DATA);
//        array.setName(parameter.getName());
//        array.setType(parameter.getType().getTypeName());
//        if (parameter.isAnnotationPresent(ApiDescribe.class)){
//            ApiDescribe apiDescribe = parameter.getDeclaredAnnotation(ApiDescribe.class);
//            array.setDescribe(apiDescribe.value());
//        }
//
//        Class<?> cls = getDataType(parameter);//获取list中的集合
//        if (cls == null){
//            return null;
//        }
//        return handleCollection(cls, apiParameList, array);
//    }

//    private List<ApiParame> handleCollection(Class<?> cls, List<ApiParame> apiParameList, ApiParame array){
//
//        if (cls == null){
//            return null;
//        }
//        List<ApiParame> apiParameList2 = null;
//        ApiParame apiParame = new ApiParame();
//        apiParame.setName(cls.getSimpleName());
//        Map<String, Object> map = apiBase.getParamValue();//参数描述,且是否为必填项目
//
//        if (ApiUtils.isData(cls)){//判断是否为内嵌的集合或则数组
//            apiParame.setApiType(ApiDataType.DATA);
//            apiParame.setIsRequired(false);
//            apiParame.setType(cls.getTypeName());
//            apiParameList2 = handleCollection(getDataType(cls), new ArrayList<>(), apiParame);
//        }else if (ApiUtils.isBaseString(cls)){
//            apiParame.setApiType(ApiDataType.BASE);
//            String reName = cls.getSimpleName();
//            apiParame.setName(reName);
//            apiParame.setIsRequired(false);
//            apiParame.setType(cls.getTypeName());
//            if (map != null) {
//                Object value = map.get(reName);
//                if (value != null) {
//                    apiParame.setValue(value);
//                }
//                value = map.get(reName + "*");
//                if (value != null) {
//                    apiParame.setIsRequired(true);//必填项目
//                    apiParame.setValue(value);
//                }
//            }
//            apiParameList2 = new ArrayList<>();
//            apiParameList2.add(apiParame);
//        }else if (ApiUtils.isModel(cls)){
//            apiParame.setType(cls.getTypeName());
//            apiParame.setApiType(ApiDataType.MODEL);
//            apiParame.setIsRequired(isRequired);
//            apiParameList2 = handleModel(cls);
//        }
//        array.setIsRequired(isRequired);
//        array.setParameList(apiParameList2);
//        apiParameList.add(array);
//        return apiParameList;
//    }

//    public static Class<?> getDataType(Class<?> cls) {
//        if (cls.isArray()){
//            return cls.getComponentType();
//        }else {
//            Type type = cls.getGenericSuperclass();
//            if (!(type instanceof ParameterizedType)){//防止类型转换异常
//                return null;
//            }
//            ParameterizedType pt = (ParameterizedType)type;//
//
//            return  (Class<?>)  pt.getActualTypeArguments()[0];
//        }
//    }

//    public static Class<?> getDataType(Field field) {
//        if (field.getClass().isArray()){
//            return field.getType().getComponentType();
//        }else {
//           Type type =  field.getGenericType();
//            if (!(type instanceof ParameterizedType)){//防止类型转换异常
//                return null;
//            }
//            ParameterizedType pt = (ParameterizedType)type;//
//            return  (Class<?>)  pt.getActualTypeArguments()[0];
//        }
//    }

//    public static Class<?> getDataType(Parameter parameter) {
//        if (parameter.getType().isArray()){
//            return parameter.getType().getComponentType();
//        }else {
//            Type type = parameter.getParameterizedType();
//            if (!(type instanceof ParameterizedType)){//防止类型转换异常
//                return null;
//            }
//            ParameterizedType pt = (ParameterizedType)type;//
//            return  (Class<?>)pt.getRawType();//获取list中的集合
//        }
//    }

    /**
     * 处理集合和数组的内嵌类型
     * @param cls
     * @return
     */
//    private List<ApiParame> inline(Class<?> cls){
//        return null;
//    }
//
//    private List<ApiParame> handleBase(Parameter parameter){
//
//        List<ApiParame> apiParame = new ArrayList<>();
//        Map<String, Object> map = apiBase.getParamValue();//参数描述,且是否为必填项目
//        ApiParame parame = new ApiParame();
//
//        if (parameter.isAnnotationPresent(ApiDescribe.class)) {
//            ApiDescribe  describe = parameter.getDeclaredAnnotation(ApiDescribe.class);
//            parame.setDescribe(describe.value());//设置备注信息
//        }
//        String reName = parameter.getName();//获取名称
//        parame.setApiType(ApiDataType.BASE);
//        parame.setIsRequired(false);
//        parame.setName(reName);
//        if (map != null) {
//            Object value = map.get(reName);
//            if (value != null) {
//                parame.setValue(value);
//            }
//            value = map.get(reName + "*");
//            if (value != null) {
//                parame.setIsRequired(true);//必填项目
//                parame.setValue(value);
//                if (!this.isRequired){
//                    this.isRequired = true;//当实体类子子类中是否包含其子类是否包含必填项目
//                }
//            }
//        }
//        apiParame.add(parame);
//        return apiParame;
//    }

//    private List<ApiParame> handleModel(Class<?> cls){
//
//        if (isExistMap == null){
//            isExistMap = new HashMap<>();
//        }
//        Map<String, Object> map = apiBase.getParamValue();//参数描述,且是否为必填项目
//        List<ApiParame> parameList = new ArrayList<>();
//        Field [] fields = cls.getDeclaredFields();
//
//        for (int i = 0; i < fields.length; i++) {//需要做类型判断下。
//            Class<?> typeCls = fields[i].getType();
//            ApiParame parame = new ApiParame();
//            boolean isExist = true;
//
//            if (fields[i].isAnnotationPresent(ApiField.class)) {
//                ApiField apiField = fields[i].getDeclaredAnnotation(ApiField.class);
//                isExist = apiField.exist();
//                if (isExist){
//                    parame.setDescribe(apiField.value());//设置备注信息
//                }
//            }
//            String reName = fields[i].getName();//获取名称
//            if (isExist && ApiUtils.isBaseString(typeCls)){//处理实体类
//                parame.setApiType(ApiDataType.BASE);
//                parame.setIsRequired(false);
//                parame.setName(reName);
//                if (map != null){
//                    Object value = map.get(reName);
//                    if (value != null){
//                        parame.setValue(value);
//                    }
//                    value = map.get(reName + "*");
//                    if (value != null){
//                        parame.setIsRequired(true);//必填项目
//                        parame.setValue(value);
//                        this.isRequired = true;//当实体类子子类中是否包含其子类是否包含必填项目
//                    }
//                }
//
//            } else if (isExist && ApiUtils.isModel(typeCls)){
//                Class<?> typeModel = fields[i].getType();
//                Integer number = isExistMap.get(typeModel.getTypeName());
//                List<ApiParame> childrenList = null;
//                if (number == null){
//                    isExistMap.put(typeModel.getTypeName(), 1);
//                    childrenList = handleModel(typeModel);
//                }else if (number < 1){
//                    isExistMap.put(typeModel.getTypeName(), number + 1);
//                    childrenList = handleModel(typeModel);
//                }
//                parame.setApiType(ApiDataType.MODEL);
//                parame.setIsRequired(this.isRequired);
//                if (childrenList != null){
//                    parame.setParameList(childrenList);
//                }
//            } else if (isExist && ApiUtils.isData(typeCls) && index != 1){
//                index = 1;
//                List<ApiParame> parameList1 = new ArrayList<>();
//                ApiParame parame1 = new ApiParame();
//                parame1.setApiType(ApiDataType.DATA);
//                parame1.setName(fields[i].getName());
//                List<ApiParame> apiList = handleCollection(getDataType(fields[i]), parameList1 , parame1 );
//                parameList1.add(parame1);
//                parame.setIsRequired(isRequired);
//                parame.setApiType(ApiDataType.DATA);
//                parame.setParameList(apiList);
//
//            }
//            if (isExist){
//                parame.setName(fields[i].getName());
//            }
//            parameList.add(parame);
//        }
//        index = 0;
//        if (cls.getSuperclass() != null){
//            handleModel(cls.getSuperclass());
//        }
//
//        if (parameList.size() != 0){
//            return parameList;
//        }
//        return null;
//    }



}
