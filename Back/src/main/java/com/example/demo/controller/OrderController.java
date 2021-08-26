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

    @PostMapping("/getDetails")
    public Result<?> getDetails(@RequestBody List<OrderDetails> selected){
        Order order = new Order();
        OrderDetails orderDetails = new OrderDetails();

        order.setOrderDetails(selected);
        order.setOrderDate(LocalDate.now());
        orderMapper.insert(order);
        double total = 0.00;
        //为选中的orderDetails赋上order的id，从而order可进行多表查询,顺便计算总价
        List row = jdbcTemplate.queryForList("select * from ordersystem.t_order_details;");
        Iterator it = row.iterator();
        for (int i = 0; i < selected.size(); i++){
            total += selected.get(i).getPrice() * selected.get(i).getAmount();
            while (it.hasNext()){
                Map foodMap = (Map) it.next();
                Integer foodAmount = Integer.parseInt(foodMap.get("amount").toString());
                if (foodMap.get("food_name").equals(selected.get(i).getFoodName()) && foodAmount == selected.get(i).getAmount() || (foodMap.get("tip").equals(selected.get(i).getTip()) || foodMap.get("tip") == null)){
                    orderDetails.setOrderId(order.getId());
                    orderDetails.setFoodName(selected.get(i).getFoodName());
                    orderDetails.setAmount(selected.get(i).getAmount());
                    String sql = "update ordersystem.t_order_details set `order_id`=? where food_name=? and amount=?";
                    Object foodObj[] = new Object[] {orderDetails.getOrderId(), orderDetails.getFoodName(), orderDetails.getAmount()};
                    jdbcTemplate.update(sql,foodObj);
                    break;
                }
            }
        }
        order.setTotalPrice(total);
        System.out.println("-----------------------------");
        System.out.println(order.getId());
        System.out.println(order.getTotalPrice());
        String sql_total = "update ordersystem.t_order set `total_price`=? where id=?";
        Object totalObj[] = new Object[] {order.getTotalPrice(), order.getId()};
        jdbcTemplate.update(sql_total,totalObj);
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
