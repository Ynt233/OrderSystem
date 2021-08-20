package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 新增一条订单信息
    @PostMapping
    public Result<?> save(@RequestBody Order order){
        List row = jdbcTemplate.queryForList("select * from ordersystem.t_order;");
        Iterator it = row.iterator();
        System.out.println("我执行了！");
        while (it.hasNext() || !it.hasNext()){
            Map foodMap = (Map) it.next();
//            System.out.println(foodMap.get("amount")+"\t");
            if (foodMap.get("food_name").equals(order.getFoodName())){
                String num = foodMap.get("amount").toString();
                Integer n = Integer.parseInt(num);
                order.setAmount(order.getAmount() + n);
                String sql = "update ordersystem.t_order set `amount`=? where food_name=?";
//                food_name=?, type=?, price=?,
//                order.getFoodName(), order.getType(), order.getPrice(),
                Object foodObj[] = new Object[] {order.getAmount(), order.getFoodName()};
                jdbcTemplate.update(sql,foodObj);
                break;
            }else {
                System.out.println("我插了！");
                orderMapper.insert(order);
                break;
            }
        }
        return Result.success();
    }

    // 编辑(更新)用户信息
    @PutMapping
    public Result<?> update(@RequestBody Order order){
        orderMapper.updateById(order);
        return Result.success();
    }

    //通过id进行删除操作
    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
        orderMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(orderMapper.selectById(id));
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
            wrapper.like(Order::getFoodName, search);
        }
        Page<Order> orderDetailsPage = orderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(orderDetailsPage);
    }
}
