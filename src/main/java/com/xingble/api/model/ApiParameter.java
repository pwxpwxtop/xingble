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


import com.xingble.api.enums.ApiDataType;
import lombok.Data;

import java.util.List;

/**
 * @description: 实体类描述
 * @author: pwx
 * @data: 2023/1/12 14:19
 * @version: 1.0
 */
@Data
public class ApiParameter {

    //属性描述
    private String describe;

    //属性的数据类型
    private ApiDataType apiType;

    //*数据类型
    private String type;

    //*属性的名称
    private String name;

    //默认值
    private Object value;

    //*是否为必填项目
    private Boolean isRequired;

    //实体类下的属性
    private List<ApiParameter> parameters;

    private ApiParameter generic;
}
