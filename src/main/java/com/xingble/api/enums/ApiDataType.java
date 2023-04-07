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
 * @description:
 * @author: pwx
 * @data: 2023/1/12 14:14
 * @version: 1.0
 */
public enum ApiDataType {

    STRING,//字符串类型
    NUMBER,//数字类型
    BOOLEAN,//布尔类型

    BASE,//基础类型
    MODEL,//实体类型
    FILE,//文件类型
    COLLECTION,//数组集合类
    ARRAY,
    OBJECT,
    DATA,//数组和集合
    MAP,//map类型
    OTHER;//其他类型
}
