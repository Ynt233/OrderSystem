package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;

public interface UserMapper extends BaseMapper<User> {
    Page<User> findPage(Page<User> page);

}
