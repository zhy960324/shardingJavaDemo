package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @title: OrderMapper
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:30
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
      * 创建表
      * @param tableName
      * @throws
      * @return void
      * @author zhy
      * @date 2020/5/11 16:17
      */
    @Update(" CREATE TABLE ${tableName} (\n" +
            "    id varchar(64) NOT NULL,\n" +
            "    name varchar(100) DEFAULT NULL COMMENT '名称',\n" +
            "    car_park_id varchar(64) DEFAULT NULL COMMENT '停车场id',\n" +
            "    no varchar(100) DEFAULT NULL COMMENT '订单号',\n" +
            "    create_time datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "    PRIMARY KEY (id)\n" +
            "  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试分表';")
    void createNewTable(@Param("tableName") String tableName);
}
