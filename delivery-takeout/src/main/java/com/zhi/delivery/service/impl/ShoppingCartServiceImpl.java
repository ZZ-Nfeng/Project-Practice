package com.zhi.delivery.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.entity.ShoppingCart;
import com.zhi.delivery.mapper.ShoppingCartMapper;
import com.zhi.delivery.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
     implements ShoppingCartService {
}
