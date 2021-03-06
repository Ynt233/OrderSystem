package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@TableName("t_user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String gender;

    private String department;

    @TableField(value = "phone_number")
    private Long phoneNumber;

    private String username;

    private String password;

    private Integer role;

    @TableField(exist = false)
    private List<Order> order;

    public User(Integer id, String name, String gender, String department, Long phoneNumber, String username, String password, Integer role, List<Order> order) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
        this.order = order;
    }

    public User(){};

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 姓名
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 性别
     */
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 部门
     */
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 手机号码
     */
    public Long getphoneNumber() {
        return phoneNumber;
    }
    public void setphoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", order=" + order +
                '}';
    }
}
