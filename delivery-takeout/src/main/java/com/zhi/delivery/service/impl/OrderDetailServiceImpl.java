package com.zhi.delivery.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.entity.OrderDetail;
import com.zhi.delivery.mapper.OrderDetailMapper;
import com.zhi.delivery.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
     implements OrderDetailService {
}
