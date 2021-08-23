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

    // 新增一条菜品信息
    @PostMapping
    public Result<?> save(@RequestBody Food food){
        foodMapper.insert(food);
        return Result.success();
    }

    // 编辑(更新)菜品信息
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
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String foodType,
                              @RequestParam(defaultValue = "") Integer currentDate){
        LambdaQueryWrapper<Food> wrapper = Wrappers.<Food>lambdaQuery();
        String early = "1至10日菜式";
        String middle = "11至20日菜式";
        String late = "21至月底菜式";
        String type = "面";
        if (!foodType.equals("")){
            if (foodType.equals("粥粉面")){
                wrapper.like(Food::getType, type);
            }else {
                wrapper.like(Food::getType, foodType);
            }

        }else{
            if(currentDate >= 1 && currentDate <= 10){
                wrapper.like(Food::getType, early);
            }else if (currentDate >= 11 && currentDate <= 20){
                wrapper.like(Food::getType, middle);
            }
            else if (currentDate >= 21 && currentDate <= 31){
                wrapper.like(Food::getType, late);
            }
        }
        Page<Food> foodPage = foodMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Food::getFoodName, search);
        }
        foodPage = foodMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(foodPage);
    }
}
