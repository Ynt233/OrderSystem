package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.OrderDetails;
import com.example.demo.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderDetails")
public class OrderDetailsController {
    @Resource
    OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 新增一条订单信息
    @PostMapping
    public Result<?> save(@RequestBody OrderDetails orderDetails){
        List row = jdbcTemplate.queryForList("select * from ordersystem.t_order_details;");
        Iterator it = row.iterator();
        System.out.println("我执行了！");
        //如果表里面没有东西就直接插入
        if(!it.hasNext()){
            System.out.println("我插了！");
            orderMapper.insert(orderDetails);
        }
        //如果表里有数据则查找需要更新的数据（通过名字）
        while (it.hasNext()){
            Map foodMap = (Map) it.next();
            System.out.println(foodMap.get("amount")+"\t");
            if (foodMap.get("food_name").equals(orderDetails.getFoodName())){
                String num = foodMap.get("amount").toString();
                Integer n = Integer.parseInt(num);
                orderDetails.setAmount(orderDetails.getAmount() + n);
                String sql = "update ordersystem.t_order_details set `amount`=? where food_name=?";
                Object foodObj[] = new Object[] {orderDetails.getAmount(), orderDetails.getFoodName()};
                jdbcTemplate.update(sql,foodObj);
                break;
            }else {
                System.out.println("我插了！");
                orderMapper.insert(orderDetails);
                break;
            }
        }
//        System.out.println(name);
//        if (!name.equals("cart")){

//        }

        return Result.success();
    }

    // 编辑(更新)用户信息
    @PutMapping
    public Result<?> update(@RequestBody OrderDetails orderDetails){
        orderMapper.updateById(orderDetails);
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
        LambdaQueryWrapper<OrderDetails> wrapper = Wrappers.<OrderDetails>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(OrderDetails::getFoodName, search);
        }
        Page<OrderDetails> orderDetailsPage = orderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(orderDetailsPage);
    }
}
