package com.example.demo.config.redis;

/**
 * @author 53240
 * @date 2019-10-21 18:52
 * @desc
 */
public enum RedisPrefixEnum {


    CAR_PARK_ID_CATALOG("carPark:idCatalog","当前所有的停车场id"),
    SHARDING_RULE_ORDER("sharding:rule:order","订单表分表规则纪录"),
    ;
    private final String value;
    private final String desc;

    RedisPrefixEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String getDescByValue(String value) {
        String desc = "";
        RedisPrefixEnum[] var2 = values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            RedisPrefixEnum o = var2[var4];
            if (o.getValue().equals(value)) {
                desc = o.getDesc();
            }
        }

        return desc;
    }
}
