package com.zhi.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Result<Employee> login(HttpServletRequest request, Employee employee);
}
