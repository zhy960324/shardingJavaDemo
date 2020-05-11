package com.example.demo.config.shardingconfig;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.example.demo.config.datasource.DataSourceProperties;
import com.example.demo.config.redis.RedisConfig;
import com.example.demo.config.redis.RedisPrefixEnum;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.orchestration.config.OrchestrationConfiguration;
import org.apache.shardingsphere.orchestration.reg.api.RegistryCenterConfiguration;
import org.apache.shardingsphere.shardingjdbc.orchestration.api.OrchestrationShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

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
@AutoConfigureAfter({DataSourceProperties.class, RedisConfig.class})
public class ShardingRuleConfig {

    private String defaultDataSource = DatasourceEnum.DEFAULT.getValue();

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

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
        dataSourceMap.put(defaultDataSource, dataSource0);
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
        OrchestrationConfiguration orchestrationConfig = new OrchestrationConfiguration(
                "orchestration-sharding-data-source", new RegistryCenterConfiguration("shardingLocalRegisterCenter"),
                false);
        return OrchestrationShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, p,
                orchestrationConfig);
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
        String logicTable = ShardingTableEnum.ORDER.getValue();
        String orderNodesByRedisCarPark = getActualDataNodesByCatalog(ShardingTableEnum.ORDER);
        //t_order_default 这张表是默认表，需要事先建好，防止首次启动报错
        String actualDataNodes = StringUtils.isEmpty(orderNodesByRedisCarPark) ? "ds0.t_order_default" : orderNodesByRedisCarPark;
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration(logicTable,actualDataNodes);
        //设置分表策略
        tableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("car_park_id",new CarParkShardingTableAlgorithm()));
        //根据时间将策略放进redis中,方便读取替换
        redisTemplate.opsForZSet().add(RedisPrefixEnum.SHARDING_RULE_ORDER.getValue(),actualDataNodes,new Date().getTime());
        return tableRuleConfig;
    }


    /**
      * 根据分表类型获取初始化actualDataNodes
      * @param logicTable
      * @throws
      * @return java.lang.String
      * @author zhy
      * @date 2020/5/11 14:52
      */
    public String getActualDataNodesByCatalog(ShardingTableEnum logicTable){
        String redisKey = RedisPrefixEnum.CAR_PARK_ID_CATALOG.getValue();
        //获取所有的停车场
        Set<Object> keys = redisTemplate.opsForHash().keys(redisKey);
        if (keys.isEmpty()){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        keys.forEach(obj -> {
            sb.append(defaultDataSource).append(".").append(logicTable.getValue()).append("_").append(obj.toString()).append(",");
        });
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
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
