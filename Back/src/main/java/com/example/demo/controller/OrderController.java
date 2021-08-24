package com.example.demo.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetails;
import com.example.demo.mapper.OrderDetailsMapper;
import com.example.demo.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderMapper orderMapper;
    OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Order order;

    @PostMapping("/getDetails")
    public Result<?> getDetails(@RequestBody List<OrderDetails> selected) {
//        System.out.println(LocalDate.now()+"\t");
//        System.out.println(selected+"\t");
        order.setOrderDetails(selected);
        order.setOrderDate(LocalDate.now());
//        orderMapper.insert(order);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<?> findAll() {
        return Result.success(orderMapper.selectList(null));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Order::getDepartment, search);
        }
        Page<Order> OrderPage = orderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(OrderPage);
    }
}
