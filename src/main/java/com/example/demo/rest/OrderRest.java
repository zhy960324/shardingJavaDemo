package com.example.demo.rest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.base.BaseRest;
import com.example.demo.common.base.Result;
import com.example.demo.common.utils.UUIDUtil;
import com.example.demo.pojo.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @title: OrderRest
 * @projectName shardingdemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/69:33
 */
@RestController
@RequestMapping("/order")
public class OrderRest extends BaseRest {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable String id){
        Order order = orderService.getById(id);
        return addSucResult(order);
    }

    @PostMapping("/{carParkId}")
    public Result save(@PathVariable String carParkId){
        Order order = new Order();
        order.setId(UUIDUtil.getUUID());
        order.setCarParkId(carParkId);
        order.setNo(order.getId());
        order.setCreateTime(new Date());
        orderService.save(order);
        return addSucResult();
    }

    @GetMapping("list/{carParkId}")
    public Result<Order> list(@PathVariable String carParkId){
        if("all".equals(carParkId)){
            return addSucResult(orderService.list());
        }else{
            LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Order::getCarParkId,carParkId);
            return addSucResult(orderService.list(lambdaQueryWrapper));
        }
    }

    @PostMapping("/listPage/{carParkId}")
    public Result listPage(@PathVariable String carParkId){
        Integer pageSize = 10;
        Integer pageNum = 1;
        IPage<Order> page = new Page<>(pageNum, pageSize, true);
        if("all".equals(carParkId)){
            return addSucResult(orderService.page(page));
        }else{
            LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Order::getCarParkId,carParkId);
            return addSucResult(orderService.page(page,lambdaQueryWrapper));
        }
    }
}
