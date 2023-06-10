package com.zhi.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisAccessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * @author Laurus
 * @Description
 * @ClassName DeliveryApplication
 * @date 2023-06-10 14:46
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement  // 开启事务，DishServiceImpl的saveWithFlavor方法
@EnableCaching    // 开启SpringCache注解方式的缓存功能

public class DeliveryApplication {

    @Resource
     static StringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        redisTemplate.opsForValue().set("key","1");
        SpringApplication.run(DeliveryApplication.class,args);
        log.info("项目启动成功！");

    }
}
