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


import com.xingble.api.conf.ApiConf;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: pwx
 * @data: 2023/3/9 13:29
 * @version: 1.0
 */
public class ApiConfService {

    private ApiConf conf = null;

    public static Map<String, Object> apiConf = null;
    public ApiConfService(ApiConf apiConf) {
        this.conf = apiConf;

    }

    public void initConf(){
        if (conf == null){
            return;
        }

        if (apiConf == null){
            apiConf = new HashMap<>();
        }

        apiConf.put("port", conf.getPort());//设置port
        apiConf.put("ip", conf.getIp());//设置ip
        apiConf.put("http", conf.getHttp());//设置http

    }



}
