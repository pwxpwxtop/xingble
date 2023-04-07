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
package com.xingble.api.enums;

/**
 * @description: 内容类型
 * @author: pwx
 * @data: 2023/3/6 13:40
 * @version: 1.0
 */
public enum ApiContentType {

    BODY,//application/json,//使用注解RequestBody
    PARAME,//使用注解@RequestParame类型
    PATH,//@PathVariable
    FILE,//文件数据类型
    FILES,//多个文件
    NONE,//没有天写任何数据
}
