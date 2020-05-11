package com.example.demo.config.shardingconfig;

import com.example.demo.config.datasource.DataSourceProperties;
import com.example.demo.config.redis.RedisConfig;
import com.example.demo.config.redis.RedisPrefixEnum;
import org.apache.shardingsphere.orchestration.reg.listener.DataChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * @title: ShardingService
 * @projectName shardingJavaDemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/1115:08
 */
@Component
@AutoConfigureAfter({RedisConfig.class})
public class ShardingService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ShardingRuleConfig shardingRuleConfig;

    /**
      * 替换sharding里的分表规则ActualDataNodes的值
      * @param oldRule
      * @param newRule
      * @throws
      * @return void
      * @author zhy
      * @date 2020/5/11 15:12
      */
    public void replaceActualDataNodes(String oldRule,String newRule){
        // 获取已有的配置
        String rules = LocalRegistryCenter.values
                .get("/orchestration-sharding-data-source/config/schema/logic_db/rule");
        // 修改规则
        String rule = rules.replace(oldRule, newRule);
        LocalRegistryCenter.listeners.get("/orchestration-sharding-data-source/config/schema")
                .onChange(new DataChangedEvent(
                        "/orchestration-sharding-data-source/config/schema/logic_db/rule",
                        rule, DataChangedEvent.ChangedType.UPDATED));
        LocalRegistryCenter.values.put("/orchestration-sharding-data-source/config/schema/logic_db/rule",rule);
    }

    /**
      * 获取当前的分表规则
      * @param shardingTableEnum
      * @throws
      * @return java.lang.String
      * @author zhy
      * @date 2020/5/11 15:56
      */
    public String getActualDataNodesInRedis(ShardingTableEnum shardingTableEnum){
        String redisKey = RedisPrefixEnum.SHARDING_RULE_ORDER.getValue();
        //倒序获取一条最新的纪录
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 1);
        return new ArrayList<>(objects).get(0).toString();
    }

    /**
      * 根据redis中存储的停车场id获取分表规则
      * @param shardingTableEnum
      * @throws
      * @return java.lang.String
      * @author zhy
      * @date 2020/5/11 16:09
      */
    public String getActualDataNodesByCatalog(ShardingTableEnum shardingTableEnum){
        return shardingRuleConfig.getActualDataNodesByCatalog(shardingTableEnum);
    }
}
