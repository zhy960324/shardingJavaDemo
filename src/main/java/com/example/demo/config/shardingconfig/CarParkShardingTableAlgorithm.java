package com.example.demo.config.shardingconfig;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @title: CarParkShardingTableAlgorithm
 * @projectName shardingdemo
 * @description: 按停车场id分表
 * @author zhy
 * @date 2020/5/611:25
 */
public class CarParkShardingTableAlgorithm implements PreciseShardingAlgorithm<String> {


    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        StringBuilder sb = new StringBuilder();
        String value = preciseShardingValue.getValue();
        String logicTableName = preciseShardingValue.getLogicTableName();
        sb.append(logicTableName).append("_").append(value);
        return sb.toString();
    }
}

