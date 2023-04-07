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
package com.xingble.api.core.init;


import java.lang.reflect.Field;

/**
 * @description: 用来处理字段
 * @author: pwx
 * @data: 2023/2/27 16:56
 * @version: 1.0
 */
public interface ApiHandleFieldImpl {


    void handleField(Field field);

    void handleNumber(Field field);

    void handleString(Field field);

    void handleBoolean(Field field);
    /**
     * 出示化默认值以及名称
     * @param name 字段名
     */
    void initDefaultValue(String name);
}
