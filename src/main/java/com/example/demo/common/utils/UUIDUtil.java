package com.example.demo.common.utils;

import java.util.UUID;

/**
 * @title: UUIDUtil
 * @projectName ips-parent
 * @description: uuid工具类
 * @author zhy
 * @date 2020/3/412:17
 */
public class UUIDUtil {

    private UUIDUtil(){}

    /**
      * @description: 获取uuid
      * @throws
      * @return
      * @author zhy
      * @date 2020/3/4 12:18
      */
    public static String getUUID(){
        UUID uuid  =  UUID.randomUUID();
        return uuid.toString().replaceAll("\\-", "");
    }
}
