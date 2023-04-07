package com.example.fastservice.mapper;

import com.example.fastservice.model.Dep;
import com.fastservice.mapper.FastMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: pwx
 * @data: 2022/9/14 23:32
 * @version: 1.0
 */
@org.apache.ibatis.annotations.Mapper
public interface DepMapper extends FastMapper<Dep> {

    @Select("")
    String getName();
}
