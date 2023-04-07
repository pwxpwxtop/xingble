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
package com.xingble.api.model;


import com.xingble.api.enums.ApiBaseType;
import com.xingble.api.enums.ApiDataType;
import com.xingble.api.enums.ApiRequestType;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: pwx
 * @data: 2023/2/18 18:50
 * @version: 1.0
 */
@Data
public class ApiBase {

    //唯一id*
    private String id;

    //与id配合,关联父级*
    private String parentId;

    //文件夹或者文件的描述名称*
    private String describe;

    //接口路径*
    private List<String> path;

    //接口请求数据类型*
    private ApiRequestType requestType;

    //api的类型,文件类型还是文件夹类型*
    private ApiBaseType baseType;

    //参数说明
    private List<ApiParame> parameters;

    //接口参数个数*
    private Integer length;



    //接口参数名称*
    private List<String> paramName;

    //接口的参数的数据类型*
    private List<ApiDataType> paramApiDataType;

    //接口参数描述 *
    private Map<String, String> paramDescribe;

    private List<List<ApiParame>> parameList;

    //所有的参数名称
    private Set<String> paramAll;

    //参数json数据
    private String json;


    //key: 参数名称, val: 默认值,必填项目 *
    private Map<String, Object> paramValue;

//    //key:参数名称, val: 必填项目
//    private Map<String, Boolean> paramRequired;
//
//    //所有参数的数据类型
//    private Map<String, ApiDataType> paramType;

    private List<ApiBase> children;


    //文件夹是否打开,默认打开
    private boolean open = true;

    //表示数据源来自后台
    private boolean origin = true;

    public boolean isEmpty(){
        return id == null;
    }

}
