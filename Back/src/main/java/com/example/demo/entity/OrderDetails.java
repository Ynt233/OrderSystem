package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order_details")
public class OrderDetails {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "food_name")
    private String foodName;

    private BigDecimal price;

    private String type;

    private Integer amount;

    private String tip;

    private Integer orderId;

    public OrderDetails(Integer id, String foodName, BigDecimal price, String type, Integer amount, String tip, Integer orderId) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.type = type;
        this.amount = amount;
        this.tip = tip;
        this.orderId = orderId;
    }

    public OrderDetails() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", foodName='" + foodName + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", tip='" + tip + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
