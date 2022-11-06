package com.hust.reggie_takeout.filter;

import com.alibaba.fastjson.JSON;
import com.hust.reggie_takeout.common.BaseContext;
import com.hust.reggie_takeout.common.R;
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

    //路径匹配器，适配通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//        Long id = Thread.currentThread().getId();
//        log.info("当前线程id为{}",id);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的地址
        String requestURI = request.getRequestURI();

        log.info("本次请求的uri地址为{}",requestURI);
        //2.创建不需要拦截的路径数组
        String[] noFilterURI = new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //3.当请求uri不需要拦截时,放行
        boolean result = check(noFilterURI, requestURI);
        if (result){
            log.info("本uri{}无需处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1.当用户处于登录状态时，放行
        if (request.getSession().getAttribute("employee") != null){
            Long id = (Long) request.getSession().getAttribute("employee");
            log.info("用户已登录,用户id为{}",id);

            //在ThreadLocal中保存当前用户id
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request,response);
            return;
        }

        //4-2.当用户使用手机验证码登录时，放行
        if (request.getSession().getAttribute("user") != null){
            Long userId = (Long) request.getSession().getAttribute("user");
            log.info("用户已登录,用户id为{}",userId);

            //在ThreadLocal中保存当前用户id
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5.如果未登录则返回未登录结果，通过输出流方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("未登录")));
        return;
    }

    /**
     * 判断本次请求是否放行
     * @param noFilterURI
     * @param requestURI
     * @return
     */
    public boolean check(String[] noFilterURI,String requestURI){
        for (String URI : noFilterURI){
            boolean b = PATH_MATCHER.match(URI, requestURI);
            if (b){
                return true;
            }
        }
        return false;
    }
}
