package com.hust.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hust.reggie_takeout.common.R;
import com.hust.reggie_takeout.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
