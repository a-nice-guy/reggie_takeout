package com.hust.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hust.reggie_takeout.common.CustomException;
import com.hust.reggie_takeout.entity.Category;
import com.hust.reggie_takeout.entity.Dish;
import com.hust.reggie_takeout.entity.Setmeal;
import com.hust.reggie_takeout.mapper.CategoryMapper;
import com.hust.reggie_takeout.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private SetmealServiceImpl setmealService;

    @Override
    public void remove(Long id) {

        //判断该删除分类是否关联了菜品,若已经关联，则抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //构造查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            //已关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //判断该删除分类是否关联了套餐,若已经关联，则抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //构造查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            //已关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //执行删除操作
        super.removeById(id);

    }
}
