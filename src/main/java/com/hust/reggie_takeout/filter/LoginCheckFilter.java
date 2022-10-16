package com.hust.reggie_takeout.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //路径匹配器，适配通配符
        AntPathMatcher PATHMATCHER = new AntPathMatcher();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的地址
        String requestURI = request.getRequestURI();
        //2.创建不需要拦截的路径数组
        String[] noFilterUri = new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout"
        };

        //3.当请求uri不需要拦截时

        //4.当用户处于登录状态时

        //5.

        log.info("拦截到url为{}的请求",request.getRequestURI());
        filterChain.doFilter(request,response);
    }


    public boolean check(String[] noFilterUri,String requestURI){
        return true;
    }
}
