package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends BaseMapper<Order> {
    Page<Order> findPage(Page<Order> page);
    Page<Order> findByDetailsId(@Param("detailsId") Integer detailsId);
}
