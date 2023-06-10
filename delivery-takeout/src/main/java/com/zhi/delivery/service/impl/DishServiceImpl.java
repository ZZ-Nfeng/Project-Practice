package com.zhi.delivery.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.common.MyCustomException;
import com.zhi.delivery.dto.DishDto;
import com.zhi.delivery.entity.Dish;
import com.zhi.delivery.entity.DishFlavor;
import com.zhi.delivery.mapper.DishMapper;
import com.zhi.delivery.service.DishFlavorService;
import com.zhi.delivery.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


}
