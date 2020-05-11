package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.CarPark;

/**
 * @title: OrderService
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:31
 */
public interface CarParkService extends IService<CarPark> {

    boolean save(CarPark carPark);
}
