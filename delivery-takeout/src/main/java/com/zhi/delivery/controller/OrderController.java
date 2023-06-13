package com.zhi.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zhi.delivery.common.BaseContext;
import com.zhi.delivery.common.MyCustomException;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.dto.OrdersDto;
import com.zhi.delivery.entity.*;
import com.zhi.delivery.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {

        log.info("订单信息:" + orders.toString());


        // 获取当前用户的id
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的 购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);

        // 一个用户可能下单了 多个菜品或套餐，故应该查询到的是一个 购物车类型的list
        List<ShoppingCart> cartList = shoppingCartService.list(queryWrapper);

        if (cartList == null || cartList.size() == 0){
            throw new MyCustomException("购物车为空，不能下单！");
        }

        // 查询用户数据
        User user = userService.getById(userId);

        // 查询用户的派送地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null){
            throw new MyCustomException("您的地址信息有误，暂不能下单!");
        }

        long orderId = IdWorker.getId();  // 订单号

        //  购物车中 商品 的总金额 需要保证在多线程的情况下 也是能计算正确的，故需要使用原子类
        AtomicInteger amount = new AtomicInteger(0);

        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask<AtomicInteger> future = new FutureTask<>(new Callable<AtomicInteger>() {
            @Override
            public AtomicInteger call() throws Exception {
                // 异步任务代码
                List<OrderDetail> orderDetails = cartList.stream().map((item) -> {
                    OrderDetail orderDetail = new OrderDetail();

                    orderDetail.setOrderId(orderId);
                    orderDetail.setName(item.getName());
                    orderDetail.setImage(item.getImage());

                    orderDetail.setDishId(item.getDishId());
                    orderDetail.setSetmealId(item.getSetmealId());
                    orderDetail.setDishFlavor(item.getDishFlavor());
                    orderDetail.setNumber(item.getNumber());
                    orderDetail.setAmount(item.getAmount());

                    amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

                    return orderDetail;

                }).collect(Collectors.toList());
                // 向订单明细表插入数据，多条数据
                orderDetailService.saveBatch(orderDetails);
                return amount;
            }
        });
        executor.execute(future);
        FutureTask<String> future1 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // 清空购物车数据
                shoppingCartService.remove(queryWrapper);
                return null;
            }
        });
        executor.execute(future1);

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额，需要 遍历购物车，计算相关金额来得到
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


        // 向订单表插入数据,一条数据，插入数据之前，需要填充如上属性
        ordersService.save(orders);    //  --> ordersService.save(orders);

        return Result.success("已成功下单!");
    }


    //    http://localhost:8181/order/page?page=1&pageSize=10&number=11
    @GetMapping("/page")
    public Result<Page> showPage(int page, int pageSize, Long number,String beginTime,String endTime) {

        Page<Orders> ordersPage = new Page(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null, Orders::getNumber, number)
                .gt(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime)
                .lt(StringUtils.isNotEmpty(endTime),Orders::getOrderTime,endTime);

        ordersService.page(ordersPage, queryWrapper);
        return Result.success(ordersPage);
    }

    // http://localhost:8181/order/userPage?page=1&pageSize=5
    @GetMapping("/userPage")
    public Result<Page> page(int page, int pageSize){
        //分页构造器对象
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //这里树直接把分页的全部结果查询出来，没有分页条件
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        this.ordersService.page(ordersPage,queryWrapper);

        //对OrderDto进行需要的属性赋值
        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> orderDtoList = records.stream().map((item) ->{
            OrdersDto orderDto = new OrdersDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = getOrderDetailsByOrderId(orderId);
            BeanUtils.copyProperties(item,orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(orderDtoList);
        return Result.success(ordersDtoPage);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return orderDetailList;
    }

    @PutMapping
    public Result<String> updateStatus(Orders orders){
        ordersService.updateById(orders);
        return Result.success("修改成功");
    }
}
