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


import com.xingble.api.model.ApiBase;

/**
 * @description: 用户返回list数据
 * @author: pwx
 * @data: 2023/2/21 19:38
 * @version: 1.0
 */
public interface ApiListServiceImpl {

    ApiBase recursiveList(String id, ApiBase base);

    void circularList();

    void fileList();
}
