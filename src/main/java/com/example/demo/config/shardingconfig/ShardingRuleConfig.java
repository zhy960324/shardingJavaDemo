package com.example.demo.config.shardingconfig;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.example.demo.config.datasource.DataSourceProperties;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @title: ShardingRuleConfig
 * @projectName shardingJavaDemo
 * @description: TODO
 * @author zhy
 * @date 2020/5/910:23
 */
@Configuration
@AutoConfigureAfter(DataSourceProperties.class)
public class ShardingRuleConfig {

    @Autowired
    private DataSourceProperties properties;

    /**
      * shardingjdbc数据源
      * @param
      * @throws
      * @return javax.sql.DataSource
      * @author zhy
      * @date 2020/5/9 10:33
      */
    @Bean
    public DataSource dataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        //多数据源配置
        //数据源1
        DruidDataSource dataSource0 = druidDataSource();
        dataSourceMap.put("ds0", dataSource0);
        //数据源2
//        DruidDataSource dataSource1 = createDb1();
//        dataSourceMap.put("ds1", dataSource1);

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        //订单表分片规则
        TableRuleConfiguration orderRuleConfig = orderRuleConfig();
        shardingRuleConfig.getTableRuleConfigs().add(orderRuleConfig);
        //可以继续用add添加分片规则
        //shardingRuleConfig.getTableRuleConfigs().add(orderRuleConfig);
        //多数据源一定要指定默认数据源，只有一个数据源就不需要
        //shardingRuleConfig.setDefaultDataSourceName("ds0");
        Properties p = new Properties();
        //打印sql语句，生产环境关闭
        p.setProperty("sql.show",Boolean.TRUE.toString());
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, p);
        return dataSource;
    }

    /**
     * 订单分片规则
     * @param
     * @throws
     * @return io.shardingjdbc.core.api.config.TableRuleConfiguration
     * @author zhy
     * @date 2020/5/7 10:28
     */
    private TableRuleConfiguration orderRuleConfig(){
        /** 分表规则，第一个参数logicTable 是逻辑表表名，逻辑表和真实的数据结构表，具有相同的数据结构，
            第二个参数actualDataNodes 指的是物理表的范围，这里的意思就是 t_order_0 到 t_order_1的3张真实表
            每次查询mapper文件里写的sql语句实际查询就只需要指向逻辑表，
            例如我这里的写法 select * from t_order 实际上sharding jdbc 会去查询t_order_0 到 t_order_1的3张真实表数据
         */
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration("t_order","ds0.t_order_${0..1}");
        //分库策略
        //tableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("city_id", "ds${city_id % 2}"));
        //设置分表策略
        tableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("car_park_id",new CarParkShardingTableAlgorithm()));
        return tableRuleConfig;
    }


    /**
     * 获取druid数据库链接
     * @param
     * @throws
     * @return com.alibaba.druid.pool.DruidDataSource
     * @author zhy
     * @date 2020/5/7 10:29
     */
    private DruidDataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setInitialSize(properties.getInitialSize());
        dataSource.setMinIdle(properties.getMinIdle());
        dataSource.setMaxActive(properties.getMaxActive());
        dataSource.setMaxWait(properties.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        String validationQuery = properties.getValidationQuery();
        if (validationQuery != null && !"".equals(validationQuery)) {
            dataSource.setValidationQuery(validationQuery);
        }
        dataSource.setTestWhileIdle(properties.isTestWhileIdle());
        dataSource.setTestOnBorrow(properties.isTestOnBorrow());
        dataSource.setTestOnReturn(properties.isTestOnReturn());
        if (properties.isPoolPreparedStatements()) {
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
        }
        String connectionPropertiesStr = properties.getConnectionProperties();
        if (connectionPropertiesStr != null && !"".equals(connectionPropertiesStr)) {
            Properties connectProperties = new Properties();
            String[] propertiesList = connectionPropertiesStr.split(";");
            for (String propertiesTmp : propertiesList) {
                String[] obj = propertiesTmp.split("=");
                String key = obj[0];
                String value = obj[1];
                connectProperties.put(key, value);
            }
            dataSource.setConnectProperties(connectProperties);
        }
        dataSource.setUseGlobalDataSourceStat(properties.isUseGlobalDataSourceStat());
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        //打开日志记录过滤器，可通过log4j2,记录sql   application.yml中配置【logging:config: classpath:logConfig/log4j2.xml】
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setStatementCreateAfterLogEnabled(false);
        slf4jLogFilter.setStatementCloseAfterLogEnabled(false);
        slf4jLogFilter.setResultSetOpenAfterLogEnabled(false);
        slf4jLogFilter.setResultSetCloseAfterLogEnabled(false);
        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter);
        filters.add(new StatFilter());
        filters.add(slf4jLogFilter);

        dataSource.setProxyFilters(filters);
        return dataSource;
    }
}
