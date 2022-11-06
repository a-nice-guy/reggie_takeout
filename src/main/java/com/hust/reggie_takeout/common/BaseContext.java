package com.hust.reggie_takeout.common;

/**
 * 基于ThreadLocal的封装工具类，用于保存和获取当前用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
