package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.config.redis.RedisPrefixEnum;
import com.example.demo.config.shardingconfig.ShardingRuleConfig;
import com.example.demo.config.shardingconfig.ShardingService;
import com.example.demo.config.shardingconfig.ShardingTableEnum;
import com.example.demo.dao.CarParkMapper;
import com.example.demo.pojo.CarPark;
import com.example.demo.service.CarParkService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * @title: OrderServiceImpl
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:31
 */
@Service
public class CarParkServiceImpl extends ServiceImpl<CarParkMapper, CarPark> implements CarParkService {
    @Autowired
    private CarParkMapper carParkMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ShardingService shardingService;

    @Autowired
    private OrderService orderService;


    @Override
    @Transactional
    public boolean save(CarPark carPark) {
        int insert = carParkMapper.insert(carPark);
        createTable(carPark.getId(),ShardingTableEnum.ORDER);
        saveRedis(carPark);
        return insert == 1;
    }

    private void saveRedis(CarPark carPark){
        //获取老规则
        String oldOrderRule = shardingService.getActualDataNodesInRedis(ShardingTableEnum.ORDER);
        //将新增的停车场信息放入redis
        String allCarParkIdKey = RedisPrefixEnum.CAR_PARK_ID_CATALOG.getValue();
        redisTemplate.opsForHash().put(allCarParkIdKey,carPark.getId(),carPark.getName());
        //获取新规则
        String newOrderRule = shardingService.getActualDataNodesByCatalog(ShardingTableEnum.ORDER);
        shardingService.replaceActualDataNodes(oldOrderRule,newOrderRule);
        //根据时间将策略放进redis中,方便读取替换
        redisTemplate.opsForZSet().add(RedisPrefixEnum.SHARDING_RULE_ORDER.getValue(),newOrderRule,new Date().getTime());
    }

    /**
      * 创建表
      * @param carParkId
      * @param shardingTableEnum
      * @throws
      * @return void
      * @author zhy
      * @date 2020/5/11 16:21
      */
    private void createTable(String carParkId,ShardingTableEnum shardingTableEnum){
        String orderTableName = shardingTableEnum.getValue() + "_" + carParkId;
        orderService.createNewTable(orderTableName);
    }
}
