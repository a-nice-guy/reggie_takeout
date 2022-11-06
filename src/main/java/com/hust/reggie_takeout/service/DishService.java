package com.hust.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hust.reggie_takeout.dto.DishDto;
import com.hust.reggie_takeout.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);
}
