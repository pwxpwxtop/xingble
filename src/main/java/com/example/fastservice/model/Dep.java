package com.example.fastservice.model;


import com.xingble.api.animation.ApiModel;
import com.xingble.api.animation.ApiField;
import com.fastservice.animation.*;
import com.fastservice.model.ModelAuto;
import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: pwx
 * @data: 2022/9/14 23:31
 * @version: 1.0
 */
@Data
@Table("dep")
@ApiModel(value = "部门表说明")
public class Dep extends ModelAuto {


    @RequiredSelect
    @RequiredInsert
    @Filter(regex = "12", regexStr = "😂")
    @ApiField("姓名")
    private String name;

    @Def("1")
    @Unique(msg = "行别重复")
    @ApiField("性别")
    private String sex;


    @ApiField("年龄")
    private String age;

    @ApiField(value = "实体类")
    private Dep dep;

    @ApiField(value = "集合dep")
    private Map<String, ?> depList;



}
