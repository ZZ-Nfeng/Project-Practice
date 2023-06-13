package com.zhi.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhi.delivery.common.BaseContext;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.entity.ShoppingCart;
import com.zhi.delivery.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> addToCart(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车中的数据:{}"+shoppingCart.toString());
        
       //获取当前用户
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        //判断当前菜品在没在数据库里，在数量加一，不在添加进去
        if(shoppingCart.getDishId()!=null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        //存在，数量加1
        if(shoppingCartServiceOne!=null){
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(shoppingCart);
            return Result.success(shoppingCartServiceOne);
        }
        //不存在,保存进数据库
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);
        return Result.success(shoppingCart);
    }

    // 在购物车中删减订单
    @PostMapping("/sub")
    public Result<String> subToCart(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车中的数据:{}"+shoppingCart.toString());

        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询当前菜品或套餐是否 在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());  // 根据登录用户的 userId去ShoppingCart表中查询该用户的购物车数据

        if (dishId != null){ // 添加进购物车的是菜品，且 购物车中已经添加过 该菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }


        ShoppingCart oneCart = shoppingCartService.getOne(queryWrapper);

        if (oneCart != null){
            Integer number = oneCart.getNumber();
            if (number != 0){
                oneCart.setNumber(number - 1);
                shoppingCartService.updateById(oneCart);
            }else {
                shoppingCartService.remove(queryWrapper);
            }

        }
        return Result.success("成功删减订单!");
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.gt(ShoppingCart::getNumber,0);
        // 最晚下单的 菜品或套餐在购物车中最先展示
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> cleanCart(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // DELETE FROM shopping_cart WHERE (user_id = ?)
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());


        shoppingCartService.remove(queryWrapper);

        return Result.success("成功清空购物车！");
    }


}
