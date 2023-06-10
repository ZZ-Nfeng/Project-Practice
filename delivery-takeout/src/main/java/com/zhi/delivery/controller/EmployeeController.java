package com.zhi.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhi.delivery.common.BaseContext;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.entity.Employee;
import com.zhi.delivery.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author Laurus
 * @Description
 * @ClassName EmployeeController
 * @date 2023-06-10 17:08
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.login(request,employee);
    }
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("安全退出成功！");
    }

    @PostMapping
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee) {
        String password = employee.getPassword();
        String digest = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        employee.setPassword(digest);
        employeeService.save(employee);
        return Result.success("添加成功");
    }
    @GetMapping("/page")
    public Result<Page> pageShow(int page, int pageSize, String name){

        Page<Employee> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>()
                .like(name != null, Employee::getName, name);

        employeeService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    @PutMapping
    public Result<String> update(@RequestBody Employee employee){
        //如果需要修改管理员，是不能修改的
        if(employee.getId()==1){
            return Result.error("不能禁用admin账号");
        }
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功！");

    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return Result.success(employee);
        }
        return Result.error("没有查询到员工信息！");
    }
}
