package com.example.fastservice.controller;


import com.xingble.api.animation.ApiValue;
import com.xingble.api.core.ApiListService;
import com.example.fastservice.model.Dep;
import com.xingble.api.animation.ApiMethod;
import com.xingble.api.animation.ApiController;
import com.fastservice.query.Select;
import com.fastservice.r.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @description:
 * @author: pwx
 * @data: 2022/9/14 23:34
 * @version: 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/api/dep")
@ApiController(value = "部门表列表")
@CrossOrigin
public class DepController  {



    @Resource
    private Select select;


    @RequestMapping("/hello")
    public R getData(String name){
        return R.ok("你好世界:" + name );
    }

    @PostMapping(path = {"data5", "d6"})
    @ApiMethod(value = "测试接口")
    @ApiValue("name*李白&age=12&dep=小白&sex=男&list=集合类型")
    public R<Dep> request5(@RequestBody Dep deps, String name){
        return R.ok(deps);
    }

    @RequestMapping(path = {"data6"})
    @ApiMethod(value = "测试接口")
    @ApiValue("name*李白&age=12&dep=小白&sex=男&list=集合类型")
    public R<Dep> request6(String [] arr){
        return R.ok(ApiListService.base);
    }

    @RequestMapping(path = {"file"})
    @ApiMethod(value = "测试接口")
    @ApiValue("name*李白&age=12&dep=小白&sex=男&list=集合类型")
    public R<Dep> file(MultipartFile file){
        return R.ok(ApiListService.base);
    }
//
//    @RequestMapping(path = {"data7"})
//    @ApiMethod(value = "测试接口")
//    @ApiValue("name*李白&age=12&dep=小白&sex=男&list=集合类型")
//    public R<Dep> request7(){
//        return R.ok("");
//    }

//
//    @GetMapping(path = {"data6"})
//    @ApiMethod(value = "测试接口")
//    public R<Dep> request6(){
//        return R.ok(ApiListService.circularList);
//    }
////
//    @GetMapping(path = {"data7"})
//    @ApiMethod(value = "测试接口")
//    public R<Dep> request7(){
//        return R.ok(ApiListService.fileList);
//    }
//
//
//    @GetMapping(path = {"data7Text"})
//    @ApiMethod(value = "测试接口")
//    public R<Dep> request7Test(Dep dep){
//        System.out.println(dep);
//        return R.ok(dep);
//    }

}
