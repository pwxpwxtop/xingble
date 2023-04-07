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

import lombok.Data;

import java.util.List;

/**
 * @description: api对象
 * @author: pwx
 * @data: 2023/1/9 13:42
 * @version: 1.0
 */
@Data
public class ApiObject{

    //当前id
    private String id;

    //父级id
    private String parentId;

    //请求类型
    private List<Object> methods;

    //描述
    private String describe;

    //接口
    private String path;

    //子集相关接口
    private List<Object> children;

    //保存参数
    private List<ApiParameter> parameters;

    private String param;
}
