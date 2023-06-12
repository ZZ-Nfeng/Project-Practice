package com.zhi.delivery.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);

        Dish dish = new Dish();
        BeanUtil.copyProperties(dishDto,dish);
        dishService.save(dish);

        return Result.success("添加成功");

    }


    @GetMapping("/page")
    public Result<Page> pageShow(int page,int pageSize,String name){
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        dishService.page(dishPage);

        List<Dish> dishList = dishPage.getRecords();

        List<DishDto> collect = dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtil.copyProperties(dish, dishDto);

            LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Category::getId, dish.getCategoryId());
            Category category = categoryService.getOne(lambdaQueryWrapper);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        BeanUtil.copyProperties(dishPage,dishDtoPage);
        dishDtoPage.setRecords(collect);
        return Result.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtil.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return Result.success(dishDto);
    }

    @Transactional
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        //更新两张表
        Dish dish = new Dish();
        BeanUtil.copyProperties(dishDto,dish);
        dishService.saveOrUpdate(dish);
        //先删除原来的数据

        Long dishId = dishDto.getId();
        dishFlavorService.remove(new LambdaUpdateWrapper<DishFlavor>().eq(DishFlavor::getDishId,dishId));

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return Result.success("删除成功");
    }




    // 根据条件(分类id)查询对应的菜品数据
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){
        Long categoryId = dish.getCategoryId();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> dishList = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishList.stream().map(dish1 -> {
            List<DishFlavor> dishFlavorList = dishFlavorService
                    .list(new LambdaUpdateWrapper<DishFlavor>()
                            .eq(DishFlavor::getDishId, dish1.getId()));

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);

            dishDto.setFlavors(dishFlavorList);
            Category category = categoryService.getOne(new LambdaUpdateWrapper<Category>().eq(Category::getId, categoryId));
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());


        return Result.success(dishDtoList);

    }

    // 改变菜品的销售状态
    @PostMapping("/status/{status}")
    public Result<String> updateSaleStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        List<Dish> dishList = dishService.listByIds(ids);
        dishList.stream().map(dish -> {
            dish.setStatus(status);
            dishService.updateById(dish);
            return dish;
        }).collect(Collectors.toList());
        return Result.success("更改成功");
    }

    @DeleteMapping
    public Result<String> batchDelete(@RequestParam("ids") List<Long> ids){
        dishService.removeByIds(ids);
        //查出所有的口味表
        for (Long id : ids) {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id);
            dishFlavorService.remove(queryWrapper);
        }
        return Result.success("删除成功");

    }

}
