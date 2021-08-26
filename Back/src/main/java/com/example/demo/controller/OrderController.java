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
import sun.security.util.AuthResources_fr;

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

    @PostMapping("/getDetails")
    public Result<?> getDetails(@RequestBody List<OrderDetails> selected){
        Order order = new Order();
        OrderDetails orderDetails = new OrderDetails();

        order.setOrderDetails(selected);
        System.out.println(order.getOrderDetails());
        order.setOrderDate(LocalDate.now());
        orderMapper.insert(order);

        List row = jdbcTemplate.queryForList("select * from ordersystem.t_order_details;");
        Iterator it = row.iterator();
        for (int i = 0; i < selected.size(); i++){
            while (it.hasNext()){
                Map foodMap = (Map) it.next();
                Integer foodAmount = Integer.parseInt(foodMap.get("amount").toString());
                System.out.println("---------------------------------------");
                System.out.println("我进来了");
                System.out.println(foodMap.get("food_name")+"和"+selected.get(i).getFoodName());
                System.out.println(foodMap.get("food_name").equals(selected.get(i).getFoodName()));
                System.out.println(foodAmount == selected.get(i).getAmount());
                System.out.println(foodMap.get("tip").equals(selected.get(i).getTip()));
                System.out.println(selected.get(i).getId());
                if (foodMap.get("food_name").equals(selected.get(i).getFoodName()) && foodAmount == selected.get(i).getAmount() || (foodMap.get("tip").equals(selected.get(i).getTip()) || foodMap.get("tip") == null)){
                    orderDetails.setOrderId(order.getId());
                    orderDetails.setFoodName(selected.get(i).getFoodName());
                    orderDetails.setAmount(selected.get(i).getAmount());
                    String sql = "update ordersystem.t_order_details set `order_id`=? where food_name=? and amount=?";
                    Object foodObj[] = new Object[] {orderDetails.getOrderId(), orderDetails.getFoodName(), orderDetails.getAmount()};
                    jdbcTemplate.update(sql,foodObj);
                    System.out.println("我执行了");
                    break;
                }
            }
        }
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(orderMapper.selectById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
        orderMapper.deleteById(id);
        return Result.success();
    }

//    @PostMapping("/getDetailsId")
//    public Result<?> getDetailsId(@RequestBody List<OrderDetails> selected) {
//
//        return Result.success(orderMapper.findByDetailsId(detailsId));
//    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Order::getDepartment, search);
        }

//        Page<Order> OrderPage = orderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        Page<Order> OrderPage = orderMapper.findPage(new Page<>(pageNum,pageSize));
        return Result.success(OrderPage);
    }
}
