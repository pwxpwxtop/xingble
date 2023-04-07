package com.example.fastservice.controller;


import com.xingble.api.animation.ApiController;
import com.xingble.api.animation.ApiDescribe;
import com.xingble.api.animation.ApiMethod;
import com.xingble.api.animation.ApiValue;
import com.xingble.api.core.ApiStart;
import com.xingble.api.core.ApiBaseService;
import com.xingble.api.core.ApiListService;
import com.example.fastservice.model.Dep;
import com.fastservice.query.Select;
import com.fastservice.r.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: pwx
 * @data: 2022/9/14 23:34
 * @version: 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/api/name")
@ApiController(value = "微信测试", parentId = "DepController")
public class WxController {



    @Resource
    private Select select;

    @PostMapping("/data")
    @ApiMethod(value = "wx测试接口")
    @ApiValue("file*23")
    public R<Dep> request(@RequestBody List<Dep> listDep){
        return R.ok(ApiStart.list);
    }

    @GetMapping(path = {"data3", "data4"})
    @ApiMethod(value = "wx测试接口")
    public R<Dep> request2(@ApiDescribe("姓名") String name, String abc, Dep dep){
        return R.ok(ApiBaseService.apiBases);
    }

    @GetMapping(path = {"data5"})
    @ApiMethod(value = "wx测试接口")
    public R<Dep> request5(@ApiDescribe("姓名") String name){
        return R.ok(ApiListService.base);
    }

    @GetMapping(path = {"data6"})
    @ApiMethod(value = "wx测试接口")
    public R<Dep> request6(){
        return R.ok(ApiListService.circularList);
    }

    @GetMapping(path = {"data7"})
    @ApiMethod(value = "wx测试接口")
    public R<Dep> request7(){
        return R.ok(ApiListService.fileList);
    }

}
