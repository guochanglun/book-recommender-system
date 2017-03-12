package com.gcl.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC 的工具类
 * <p>
 * 其中包含: 获取数据库连接, 关闭数据库资源等方法.
 */
public class JDBCTools {

    public static Connection getConnection() {
        Properties properties = new Properties();
        InputStream inStream = JDBCTools.class.getClassLoader()
                .getResourceAsStream("jdbc.properties");
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. 准备获取连接的 4 个字符串: user, password, url, jdbcDriver  
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String jdbcDriver = properties.getProperty("jdbcDriver");

        // 2. 加载驱动: Class.forName(driverClass)  
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 3.获取数据库连接  
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user,
                    password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

} 