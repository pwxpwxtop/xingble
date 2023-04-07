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
package com.xingble.api.controller;


import com.xingble.api.core.ApiConfService;
import com.xingble.api.core.ApiListService;
import com.xingble.api.core.ApiScanClass;
import com.xingble.r.R;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: pwx
 * @data: 2023/3/4 14:21
 * @version: 1.0
 */
@RestController
@RequestMapping("/FastGet")
@CrossOrigin
public class FastGetController {

    /**
     * 获取解析后的数据
     * @return
     */
    @PostMapping("/all")
    public R getTree(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "我的项目");
        map.put("verification", "openMyApi");//校验码
        map.put("version", "1.0");
        map.put("tree", ApiListService.base);//数据结构
        map.put("model", ApiScanClass.objectMap);//模型
        map.put("conf", ApiConfService.apiConf);//配置信息
        return R.ok(map);
    }

//    @GetMapping("/getData")
//    public R getData(){
//        return R.ok(ApiListService.base);
//    }
//
//    @GetMapping("/search")
//    public R getData(String id){
//        if (ApiScanClass.objectMap == null) {
//            return R.no("map为空");
//        }
//        Object o = ApiScanClass.objectMap.get(id);
//
//        if (o == null){
//            return R.no("没有找到");
//        }
//        return R.ok(o);
//    }

}
