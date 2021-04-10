package com.pikachu.common.database.core;

import com.pikachu.common.util.PikachuStrings;

/**
 * @Desc 数据库类型枚举类
 * @Date 2020/12/2 21:18
 * @Author AD
 */
public enum DatabaseType {
    MYSQL,
    ORACLE,
    SQLSERVER,
    DERBY,
    OTHERS;
    
    /**
     * JDBC协议：协议:<子协议>:<子名称>
     * 协议：JDBC URL中的协议总是jdbc</br>
     * 子协议：子协议用于标识一个数据库驱动程序
     * 子名称：一宗标识数据库的方法。子名称可以依不同的子协议而变化，用子名称的目的是为了定位数据库提供足够的信息
     * MySQL：jdbc:mysql://localhost:3306/dbname
     * Oracle：jdbc:oracle:thin:@localhost:1521:dbname
     * SQLServer：jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=dbname
     * Derby：jdbc:derby://localhost:1527/dbname;user=user;password=pwd;create=true;
     *
     * @param url
     *
     * @return
     */
    public static DatabaseType get(String url) {
        if (PikachuStrings.isNotNull(url)) {
            if (url.startsWith("jdbc:mysql")) {
                return MYSQL;
            }
            if (url.startsWith("jdbc:oracle")) {
                return ORACLE;
            }
            if (url.startsWith("jdbc:microsoft")) {
                return SQLSERVER;
            }
            if (url.startsWith("jdbc:derby")) {
                return DERBY;
            }
            return OTHERS;
        }
        return OTHERS;
    }
}
