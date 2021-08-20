package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Food;
import com.example.demo.mapper.FoodMapper;
import com.example.demo.mapper.FoodMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Resource
    FoodMapper foodMapper;

    // 新增一条用户信息
    @PostMapping
    public Result<?> save(@RequestBody Food food){
        foodMapper.insert(food);
        return Result.success();
    }

    // 编辑(更新)用户信息
    @PutMapping
    public Result<?> update(@RequestBody Food food){
        foodMapper.updateById(food);
        return Result.success();
    }

    //通过id进行删除操作
    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
        foodMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(foodMapper.selectById(id));
    }

    @GetMapping("/all")
    public Result<?> findAll() {
        return Result.success(foodMapper.selectList(null));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Food> wrapper = Wrappers.<Food>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Food::getFoodName, search);
        }
        Page<Food> foodPage = foodMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(foodPage);
    }
}
