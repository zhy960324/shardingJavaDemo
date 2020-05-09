package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: Order
 * @projectName shardingdemo
 * @description: 订单实体
 * @author zhy
 * @date 2020/5/69:26
 */
@TableName("t_order")
@Data
public class Order implements Serializable {

    // 串行版本ID
    private static final long serialVersionUID = 3310565453981832744L;

    @TableId
    private String id;

    private String name;

    private String carParkId;

    private String no;

    private Date createTime;
}
