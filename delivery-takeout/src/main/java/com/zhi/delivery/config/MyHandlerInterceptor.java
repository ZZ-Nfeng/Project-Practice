package com.zhi.delivery.config;

import com.alibaba.fastjson.JSON;
import com.zhi.delivery.common.BaseContext;
import com.zhi.delivery.common.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Laurus
 * @Description
 * @ClassName MyHandlerInterceptor
 * @date 2023-06-10 19:33
 */
public class MyHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long employee = (Long) request.getSession().getAttribute("employee");
        if(employee!=null){
            BaseContext.setCurrentId(employee);
            return true;
        }else {
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            return false;
        }
    }
}
