package com.hust.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hust.reggie_takeout.entity.User;
import com.hust.reggie_takeout.mapper.UserMapper;
import com.hust.reggie_takeout.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
