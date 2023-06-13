package com.zhi.delivery.config;

import com.alibaba.fastjson.JSON;
import com.zhi.delivery.common.BaseContext;
import com.zhi.delivery.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Laurus
 * @Description
 * @ClassName MyHandlerInterceptor
 * @date 2023-06-10 19:33
 */
@Slf4j
public class MyHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //b端
        Long employee = (Long) request.getSession().getAttribute("employee");

        if(employee!=null){
            BaseContext.setCurrentId(employee);
            return true;
        }
        //c端
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null){
            log.info("用户已经登录，用户id为:{}",userId);

            // 自定义元数据对象处理器 MyMetaObjectHandler中需要使用 登录用户id
            //   通过ThreadLocal set和get用户id
            BaseContext.setCurrentId(userId);
            return true;
        }else {
            // 5、如果未登录，则返回未登录的结果
            // 通过输出流 向客户端页面响应数据
            // 返回结果需要是 backend/js/request.js 中写好的返回结果
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            return false;
        }
    }
}
