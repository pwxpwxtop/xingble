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


import com.xingble.api.core.init.ApiListServiceImpl;
import com.xingble.api.enums.ApiBaseType;
import com.xingble.api.model.ApiBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: pwx
 * @data: 2023/2/21 19:38
 * @version: 1.0
 */
public class ApiListService implements ApiListServiceImpl {

    public static List<ApiBase> apiBases = ApiBaseService.apiBases;//所有的接口

    public static ApiBase base = null;

    public static List<ApiBase> fileList = new ArrayList<>();

    public static List<ApiBase> circularList = new ArrayList<>();

    private List<String> stringList = new ArrayList<>();

    @Override
    public ApiBase recursiveList(String id, ApiBase base) {
        List<ApiBase> apiBaseList = new ArrayList<>();
        for (ApiBase apiBase : apiBases) {
            if (apiBase.getParentId() != null && apiBase.getParentId().equals(id)){
                stringList.add(apiBase.getParentId());
                apiBaseList.add(recursiveList(apiBase.getId(), apiBase));
            }
        }
        if (apiBaseList.size() != 0 && base != null) {
            base.setChildren(apiBaseList);
        }
        return base;
    }

    @Override
    public void circularList() {
        List<ApiBase> list = apiBases;
        for (int i = 0; i < list.size(); i++) {
            ApiBase apiBase = list.get(i);
            List<ApiBase> bases = new ArrayList<>();
            for (int j = i; j < list.size(); j++) {
                if (list.get(i).getId().equals(list.get(j).getParentId()) && i != j){
                    bases.add(list.get(j));
                }
            }
            if (bases.size() != 0) {
                apiBase.setChildren(bases);
                circularList.add(apiBase);
            }
        }
    }

    @Override
    public void fileList() {
        List<ApiBase> list = apiBases;
        for (ApiBase apiBase : list) {
            if (apiBase.getBaseType() == ApiBaseType.FILE) {
                fileList.add(apiBase);
            }
        }
    }

    public void executeTree(){
        //递归
        ApiBase apiBase = new ApiBase();
        apiBase.setId("ROOT");
        base = recursiveList(apiBase.getId(), apiBase);
        //循环
        circularList();
        //文件接口列表
        fileList();
    }


}
