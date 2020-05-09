package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @title: OrderMapper
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:30
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
