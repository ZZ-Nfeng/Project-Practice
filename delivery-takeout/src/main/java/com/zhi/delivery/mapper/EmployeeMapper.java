package com.zhi.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhi.delivery.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
