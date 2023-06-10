package com.zhi.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhi.delivery.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
