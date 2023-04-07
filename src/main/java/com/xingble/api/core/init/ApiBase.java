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


import com.xingble.api.model.ApiParame;

import java.util.Map;

/**
 * @description:
 * @author: pwx
 * @data: 2023/2/27 22:45
 * @version: 1.0
 */
public abstract class ApiBase {
    private Map<String, Object> mapValue = null;

    public ApiBase(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }

    public void initDefaultValue(ApiParame apiParame ,String name) {
        if (mapValue != null) {
            Object value = mapValue.get(name);
            if (value != null) {
                apiParame.setVal(value);
            }
            value = mapValue.get(name + "*");
            if (value != null) {
                apiParame.setIsRequired(true);//必填项目
                apiParame.setVal(value);
            }
        }
    }
}
