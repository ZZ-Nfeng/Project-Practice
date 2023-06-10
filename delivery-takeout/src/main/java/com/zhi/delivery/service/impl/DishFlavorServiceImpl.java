package com.zhi.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.entity.DishFlavor;
import com.zhi.delivery.mapper.DishFlavorMapper;
import com.zhi.delivery.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
        implements DishFlavorService {
}
