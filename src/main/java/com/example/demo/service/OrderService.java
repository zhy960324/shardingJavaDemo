package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.Order;

/**
 * @title: OrderService
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:31
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建表
     * @param tableName
     * @throws
     * @return void
     * @author zhy
     * @date 2020/5/11 16:17
     */
    void createNewTable(String tableName);
}
