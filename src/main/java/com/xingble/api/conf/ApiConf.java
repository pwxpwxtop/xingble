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
package com.xingble.api.conf;


import lombok.Data;

/**
 * @description: 配置项目
 * @author: pwx
 * @data: 2023/3/2 14:59
 * @version: 1.0
 */
@Data
public class ApiConf {

    //项目明朝
    private String name = "我的项目";

    //所有的实体包位置 例如:com.xingble.api.entity
    private String [] packs= null;

    //链接请求的端口号
    private String port = "8080";

    //链接的ip地址
    private String ip = "127.0.0.1";

    //请求方式
    private String http = "http";

}
