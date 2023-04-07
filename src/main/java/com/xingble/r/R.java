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
package com.xingble.r;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingble.api.enums.CodeType;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: pwx
 * @data: 2022/9/10 17:48
 * @version: 1.0
 */
@Data
public class R<T> extends ResultLog implements Serializable {

    private CodeType code;  //返回状态,成功和失败
    private String msg; //报的错误异常消息
    private String errMsg;//异常消息类
    private Object data; //需要数据
    private Long total; //表格数量
    private Long count; //更新的数据条数
    
    public static <T> R<T> ok(Page<T> page){
        return ok(page,  null);
    }

    public static <T> R<T> ok(Page<T> page, String msg){
        R<T> r = new R<>();
        r.setCode(CodeType.OK);
        r.setCount(r.getCount());
        r.setData(page.getRecords());
        r.setMsg(msg);
        r.setTotal(page.getTotal());
        return r;
    }

    public static <T> R<T> ok(Object obj, String msg, Long count, Long total){
        R<T> r = new R<>();
        r.setCode(CodeType.OK);
        r.setCount(count);
        r.setData(obj);
        r.setMsg(msg);
        r.setTotal(total);
        return r;
    }

    //成功的消息
    public static <T> R<T> ok(){
        return ok(null, null, null, null);
    }

    public static <T> R<T> ok(String msg){
        return ok(null, msg, null, null);
    }

    //成功的消息,count 数据记录条数
    public static <T> R<T> ok(Long count){
        return ok(null, null, count, null);
    }


    public static <T> R<T> ok(Object obj){
        return ok(obj, null, null, null);
    }

    public static <T> R<T> ok(Object obj, String msg){
        return ok(obj, msg, null, null);
    }

    public static <T> R<T> no(){
        return no(null, null);
    }

    public static <T> R<T> no(String msg){
        return no(null, msg);
    }

    public static  <T> R<T> no(Object data, String msg){
        R<T> r = new R<>();
        r.setCode(CodeType.NO);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> err(Exception e){
        return err(e, null);
    }

    public static <T> R<T> err(Exception e, String msg){
        e.printStackTrace();
        logger.error(e.getMessage());
        R<T> r = new R<>();
        r.setCode(CodeType.ERR);
        r.setErrMsg(e.getMessage());
        r.setMsg(msg);
        return r;
    }

    //工具类,返回解析后的map
    private static Map<String, Object> returnMap(R r){
        Map<String, Object> maps = new HashMap<>();
        Field [] fields =  r.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object obj = null;
            try {
                obj = fields[i].get(r);
                if (obj != null){
                    maps.put(fields[i].getName(), obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return maps;
    }

}
