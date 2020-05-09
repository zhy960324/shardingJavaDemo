package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.demo.dao"})
@EnableAutoConfiguration
@ComponentScan(value = {"com.example.demo.config.**","com.example"})
public class ShardingJavaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJavaDemoApplication.class, args);
    }

}
