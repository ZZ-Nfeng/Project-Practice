package com.zhi.delivery.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhi.delivery.common.Result;
import com.zhi.delivery.entity.Category;
import com.zhi.delivery.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // @RequestBody: 将前端回传的JSON数据需要使用@RequestBody 转化为 实体对象
    @PostMapping
    public Result<String> save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page> showPage(int page, int pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        categoryService.page(categoryPage);
        return Result.success(categoryPage);
    }


    @DeleteMapping
    public Result<String> delete(Long ids){
        categoryService.removeById(ids);
        return Result.success("删除成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("分类信息 修改成功！");
    }

    // 根据条件查询分类数据
    @GetMapping("/list")
    public Result<List<Category>> categoryList(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //  只有当 category.getType()不为空，才会比较 前端传入的category的type和 实体类中 type属性是否相等
        queryWrapper.eq(type != null, Category::getType,type);

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return Result.success(list);
    }

}
