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
package com.xingble.api.utils;


import com.xingble.api.animation.ApiFile;
import com.xingble.api.animation.ApiModel;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @description:
 * @author: pwx
 * @data: 2023/1/14 12:20
 * @version: 1.0
 */
public class ApiUtils {

    /**
     * 判断是否是基础数据类型，即 int,double,long等类似格式
     */
    public static boolean isCommonDataType(Class<?> cls){
        return cls.isPrimitive();
    }

    /**
     * 判断是否是基础数据类型的包装类型
     * @param cls
     * @return
     */
    public static boolean isWrapClass(Class<?> cls) {
        try {
            return ((Class) cls.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断基础基础类型和基础包装类型
     * @param cls
     * @return
     */
    public static boolean isBaseWrapClass(Class<?> cls){
        return isCommonDataType(cls) || isWrapClass(cls);
    }

    /**
     * 判断是否为Collection类
     * @param cls
     * @return
     */
    public static boolean isCollection(Class<?> cls){
        return Collection.class.isAssignableFrom(cls) ;
    }

    /**
     * 判断是否为数组类型
     * @param cls
     * @return
     */
    public static boolean isArray(Class<?> cls){
        return cls.isArray();
    }

    /**
     * 判断是否为List或者数组类型
     * @param cls
     * @return
     */
    public static boolean isData(Class<?> cls ){
        return isCollection(cls) || isArray(cls);
    }

    /**
     * 判断是否为Map类
     * @param cls
     * @return
     */
    public static boolean isMap(Class<?> cls){
        return Map.class.isAssignableFrom(cls) ;
    }

    /**
     * 是否为集合类
     * @param cls
     * @return
     */
    public static boolean isAggregate(Class<?> cls){
        return isCollection(cls) ||isMap(cls);
    }


    /**
     * 判断是否为字符串
     * @param cls
     * @return bool
     */
    public static boolean isString(Class<?> cls){
        return String.class.equals(cls) || StringBuilder.class.equals(cls) || StringBuffer.class.equals(cls) || StringJoiner.class.equals(cls);
    }

    /**
     * 判断基础和封装及字符串类型
     * @param cls
     * @return
     */
    public static boolean isBaseString(Class<?> cls){
        return isString(cls) || isBaseWrapClass(cls);
    }

    /**
     * 判断是否包含ApiModel实体类
     * @param cls
     * @return
     */
    public static boolean isModel(Class<?> cls){
        return cls.isAnnotationPresent(ApiModel.class);
    }

    /**
     * 判断是否为文件类型
     * @param cls
     * @return
     */
    public static boolean isFile(Class<?> cls){
        return MultipartFile.class.equals(cls);
    }

    public static boolean isFiles(Class<?> cls){
        return MultipartFile[].class.equals(cls);
    }

    public static boolean isFile(Parameter parameter){
        if (parameter.isAnnotationPresent(ApiFile.class)) {
            return true;
        }
        return false;
    }

    /**
     * 是否为boolean和Boolean类型
     * @param cls
     * @return
     */
    public static boolean isBoolean(Class<?> cls){
        String name = cls.getSimpleName();
        return "boolean".equals(name) || "Boolean".equals(name);
    }

    /**
     * 是否为数字类型
     * @param cls
     * @return
     */
    public static boolean isNumber(Class<?> cls) {
        return Number.class.isAssignableFrom(cls) || cls == int.class || cls == long.class || cls == float.class || cls == short.class || cls == byte.class;
    }

    /**
     * 是否为字符串类型
     * @param cls
     * @return
     */
    public static boolean isStringClass(Class<?> cls){
        return (cls == String.class || cls == StringBuilder.class || cls == StringBuffer.class || cls == StringJoiner.class);
    }

    /**
     * 判断是否为 String [] []这种数据类型
     * @param cls
     * @return
     */

    public static boolean isArray2(Class<?> cls){
        if (cls.isArray()) {
            Class<?> clsT =  cls.getComponentType();
            return clsT.isArray();
        }
        return false;
    }

    public static boolean isCollection2(Class<?> cls){
        if (Collection.class.isAssignableFrom(cls)){


            Type t = cls.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                System.out.println(t);
                for (Type type : ((ParameterizedType) t).getActualTypeArguments()) {
                    System.out.println(type.getTypeName());
                    //output: class cn.think.in.java.clazz.loader.generics.DataClass
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        String [][] test = new String[2][2];
//        System.out.println(isArray2(test.getClass()));

        List<String> list = new ArrayList<>();
        System.out.println(isCollection2(list.getClass()));

        ArrayList<ArrayList<String>> list2 = new ArrayList<>();
        System.out.println(isCollection2(list2.getClass()));


    }


    //--------------------------------------------------------------------------------------
    /**
     * 处理地址链接长度
     * @param path
     * @return
     */
    public static String pathCheck(String path){
        if (path == null){
            return null;
        }

        if ("".equals(path.trim())){
            return path;
        }

        if (path.length() == 0) {
            return path;
        }

        path = subPath(path);
        if (!"/".equals(path.substring(0, 1))){
            path = "/" + path;
        }
        path = subLastPath(path);

        return path;
    }

    /**
     * 递归处理多余的斜杠
     * @param path
     * @return
     */
    private static  String subPath(String path){
        if (path.length() > 1){
            if ("/".equals(path.substring(1, 2))){
                path  = subPath(path.substring(1));
            }
        }
        return path;
    }

    /**
     * 处理最后面的斜杠
     * @param path
     * @return
     */
    private static String subLastPath(String path){
        if (path.length() > 2){
            if ("/".equals(path.substring(path.length() - 1))){
                path = subLastPath(path.substring(0, path.length()-1));
            }
        }
        return path;
    }
    //--------------------------------------------------------------------------------------

    public  static  String  generateRandomString()  {
        String  characters  =  "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder  sb  =  new  StringBuilder();
        Random  random  =  new  Random();
        for  (int  i  =  0;  i  <  10;  i++)  {
            int  index  =  random.nextInt(characters.length());
            char  ch  =  characters.charAt(index);
            sb.append(ch);
        }
        return  sb.toString();
    }
}
