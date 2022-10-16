package com.hust.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hust.reggie_takeout.common.R;
import com.hust.reggie_takeout.entity.Employee;
import com.hust.reggie_takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //在数据库中获取员工信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Employee::getUsername,employee.getUsername());
        Employee emp_stored = employeeService.getOne(wrapper);

        //对密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //1.如果没有查询到该员工信息，返回错误
        if (emp_stored == null){
            return R.error("用户名或密码错误，请再次输入");
        }

        //2.比较登录密码是否正确，若不匹配则返回错误
        if (!password.equals(emp_stored.getPassword())){
            return R.error("用户名或密码错误，请再次输入");
        }

        //3.查看用户登陆权限是否被禁用
        if (emp_stored.getStatus() == 0){
            return R.error("用户登录权限被禁用，无法登录");
        }

        request.getSession().setAttribute("employee",emp_stored.getId());

        return R.success(emp_stored);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功");
    }

}
