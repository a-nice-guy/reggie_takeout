package com.hust.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hust.reggie_takeout.common.R;
import com.hust.reggie_takeout.entity.Employee;
import com.hust.reggie_takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //在数据库中获取员工信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Employee::getUsername,employee.getUsername());
        Employee emp_stored = employeeService.getOne(wrapper,false);

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

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功");
    }

    /**
     * 新增员工信息
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee){
        log.info("新增员工，员工信息为：{}",employee.toString());

        //设置用户密码、创建日期和修改日期
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获取保存在作用域中的操作人的id
//        Long userID = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(userID);
//        employee.setUpdateUser(userID);

        //调用service层服务保存用户信息
        employeeService.save(employee);

        return R.success("员工添加成功");
    }

    /**
     * 分页查询员工信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {} , pageSize = {} , name = {}",page,pageSize,name);

        //创建分页构造器
        Page pageInfo = new Page<>(page,pageSize);

        //创建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        log.info(employee.toString());

//        Long id = Thread.currentThread().getId();
//        log.info("当前线程id为{}",id);

//        Long id = (long)request.getSession().getAttribute("employee");
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("用户信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){

        log.info("获取对应id的员工信息");
        Employee employee = employeeService.getById(id);

        if (employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到改员工");
    }
}
