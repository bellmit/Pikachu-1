package com.x.pikachu.common.database.pool.factory;

import com.x.pikachu.common.database.pool.core.PoolConfig;
import com.x.pikachu.common.database.pool.core.PoolType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Desc Druid数据库连接池配置
 * @Date 2020/12/4 20:56
 * @Author AD
 */
public class DruidPoolConfig extends PoolConfig {
    
    // ------------------------ 变量定义 ------------------------
    // 初始化连接数
    private int initialSize = 0;
    // 最大允许的连接数
    private int maxActive = 20;
    // 最小空闲连接数
    private int minIdle = 1;
    // 连接等待超时时间
    private long maxWait = 60000L;
    // 是否保持连接活动
    private boolean keepActive = false;
    // 连接最小生存时间
    private long minEvictableIdleTimeMillis = 1800000L;
    // 检测需要关闭的空闲连接的间隔时间
    private long timeBetweenEvictionRunsMillis = 60000L;
    // 检测连接是否可用的sql语句
    private String validationQuery = "SELECT 1";
    // 空闲时是否检测连接可用性
    private boolean testWhileIdle = true;
    // 获取连接时是否检测连接可用性
    private boolean testOnBorrow = false;
    // 归还连接时检测连接是否有效
    private boolean testOnReturn = false;
    // 当发现池中的可用实例已经用光时，需要做的动作
    private boolean exhaustedAction = true;
    // 是否缓存preparedStatement(PSCache)。对支持游标的数据库性能提升巨大，如:oracle。mysql下建议关闭
    private boolean poolPreparedStatements = false;
    
    // ------------------------ 构造方法 ------------------------
    
    /**
     * Druid数据库连接池配置
     *
     * @param poolName
     */
    public DruidPoolConfig(String poolName) {
        super(poolName);
    }
    
    @Override
    public PoolType getPoolType() {
        return PoolType.DRUID;
    }
    
    // ------------------------ 方法定义 ------------------------
    public Properties toProperties() {
        List<Field> fields = getAllFields(this.getClass(), new ArrayList<>());
        Properties prop = new Properties();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                prop.put(field.getName(), String.valueOf(field.get(this)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }
    
    public HikariPoolConfig toHikariConfig() {
        HikariPoolConfig config = new HikariPoolConfig(this.poolName);
        config.setUrl(this.url);
        config.setDriver(this.driver);
        config.setUsername(this.username);
        config.setPassword(this.password);
        
        config.setAutoCommit(true);
        config.setConnectionTestQuery(this.validationQuery);
        config.setMaximumPoolSize(this.maxActive);
        config.setMinimumIdle(this.minIdle);
        config.setConnectionTimeout(this.maxWait);
        config.setMaxLifetime(this.minEvictableIdleTimeMillis);
        config.setIdleTimeout(this.timeBetweenEvictionRunsMillis);
        return config;
    }
    
    // ------------------------ 私有方法 ------------------------
    private List<Field> getAllFields(Class<?> clazz, List<Field> fieldContainer) {
        Field[] fields = clazz.getDeclaredFields();
        fieldContainer.addAll(Arrays.asList(fields));
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            getAllFields(superclass, fieldContainer);
        }
        return fieldContainer;
    }
    
}
