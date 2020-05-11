package com.example.demo.rest;

import com.example.demo.common.base.BaseRest;
import com.example.demo.common.base.Result;
import com.example.demo.common.utils.UUIDUtil;
import com.example.demo.pojo.CarPark;
import com.example.demo.service.CarParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @title: OrderRest
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:33
 */
@RestController
@RequestMapping("/carPark")
public class CarParkRest extends BaseRest {

    @Autowired
    private CarParkService carParkService;

    @PostMapping("/save")
    public Result save(@RequestBody CarPark carPark){
        carPark.setId(UUIDUtil.getUUID());
        carPark.setCreateTime(new Date());
        carParkService.save(carPark);
        return addSucResult();
    }
}
