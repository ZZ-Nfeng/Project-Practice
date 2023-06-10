package com.zhi.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhi.delivery.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
