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

    @TableField(value = "staff_name")
    private String staffName;

    private String department;

    private List<OrderDetails> orderDetails;

    public Order(Integer id, LocalDate orderDate, String staffName, String department, List<OrderDetails> orderDetails) {
        this.id = id;
        this.orderDate = orderDate;
        this.staffName = staffName;
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

    public String getstaffName() {
        return staffName;
    }

    public void setstaffName(String staffName) {
        this.staffName = staffName;
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
                ", staffName='" + staffName + '\'' +
                ", department='" + department + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
