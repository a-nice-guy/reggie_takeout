package com.hust.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hust.reggie_takeout.entity.Employee;
import com.hust.reggie_takeout.mapper.EmployeeMapper;
import com.hust.reggie_takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
