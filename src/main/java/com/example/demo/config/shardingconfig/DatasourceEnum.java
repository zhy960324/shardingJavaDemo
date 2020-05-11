package com.example.demo.config.shardingconfig;


public enum DatasourceEnum {

    DEFAULT("ds0", "默认数据源"),

            ;


    private final String value;
    private final String desc;

    DatasourceEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    // Jackson 注解为 JsonValue 返回中文 json 描述
    public String getDesc() {
        return this.desc;
    }

    public static String getDescByValue(String value) {
        String desc = "";
        DatasourceEnum[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            DatasourceEnum o = var2[var4];
            if (o.getValue().equals(value)) {
                desc = o.getDesc();
            }
        }

        return desc;
    }

    /**
      * 根据value 获取枚举
      * @param value
      * @throws
      * @return com.cec.park.common.enums.carspaces.ExistEnum
      * @author zhy
      * @date 2020/4/14 10:07
      */
    public static DatasourceEnum getByValue(Integer value) {
        for (DatasourceEnum carSpaceTypeEnum : values()) {
            if (carSpaceTypeEnum.getValue() .equals(value) ) {
                //获取指定的枚举
                return carSpaceTypeEnum;
            }
        }
        return DEFAULT;
    }
}
