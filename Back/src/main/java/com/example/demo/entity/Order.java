package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@TableName("t_order")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "order_date")
    private LocalDate orderDate;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "user_name")
    private String userName;

    private String department;

    @TableField(exist = false)
    private List<OrderDetails> orderDetails;

    public Order(Integer id, LocalDate orderDate, Integer userId, String userName, String department, List<OrderDetails> orderDetails) {
        this.id = id;
        this.orderDate = LocalDate.now();
        this.userId = userId;
        this.userName = userName;
        this.department = department;
        this.orderDetails = orderDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getuserId() {
        return userId;
    }

    public void setuserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", department='" + department + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
