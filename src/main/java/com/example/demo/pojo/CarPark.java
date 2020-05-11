package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @title: CarPark
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/810:12
 */
@TableName("t_car_park")
@Data
public class CarPark {

    @TableId
    private String id;

    private String name;

    private Date createTime;
}
