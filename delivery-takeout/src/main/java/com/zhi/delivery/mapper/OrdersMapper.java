package com.zhi.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhi.delivery.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
