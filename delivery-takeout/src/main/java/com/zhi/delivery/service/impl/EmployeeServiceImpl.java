package com.zhi.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.entity.Employee;
import com.zhi.delivery.mapper.EmployeeMapper;
import com.zhi.delivery.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {



    @Override
    public Result<Employee> login(HttpServletRequest request, Employee employee) {
        /**  登录功能处理逻辑如下:
         1、将页面提交的密码password进行 MD5 加密处理
         2、根据页面提交的用户名username查询数据库
         3、如果没有查询到数据，则返回登录失败的结果
         4、进行密码比对，如果不一致，则返回登录失败的结果
         5、查看员工状态，如果为 已禁用状态，则返回被禁用的结果信息
         6、登录成功，将员工id 存入Session并返回登录成功的结果
         * */
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());

        Employee one = this.getOne(queryWrapper);

        if (one == null ) {
            return Result.error("用户不存在");
        }
        if(!one.getPassword().equals(password)){
            return Result.error("用户名或密码错误");
        }
        if(one.getStatus() == 0){
            return Result.error("员工被禁用");
        }
        //第一用写法就是加载进session中的操作
        request.getSession().setAttribute("employee",one);
        return Result.success(one);
        //第二中写法就是加载到redis数据库中
    }
}
