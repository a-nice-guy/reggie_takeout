package com.hust.reggie_takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hust.reggie_takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
