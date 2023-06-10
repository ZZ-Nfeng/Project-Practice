package com.zhi.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.dto.DishDto;
import com.zhi.delivery.entity.Category;
import com.zhi.delivery.entity.Dish;
import com.zhi.delivery.entity.DishFlavor;
import com.zhi.delivery.service.CategoryService;
import com.zhi.delivery.service.DishFlavorService;
import com.zhi.delivery.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){

    }


    @GetMapping("/page")
    public Result<Page> pageShow(int page,int pageSize,String name){

    }

    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id){


    }

    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){

    }




    // 根据条件(分类id)查询对应的菜品数据
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){

    }

    // 改变菜品的销售状态
    @PostMapping("/status/{status}")
    public Result<String> updateSaleStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){


    }

    @DeleteMapping
    public Result<String> batchDelete(@RequestParam("ids") List<Long> ids){

    }

}
