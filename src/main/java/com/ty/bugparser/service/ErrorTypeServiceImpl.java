package com.ty.bugparser.service;

import com.ty.bugparser.pojo.ErrorType;
import com.ty.bugparser.utils.DBUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ErrorTypeServiceImpl implements ErrorTypeService {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("classify.db.driver");
        url = properties.getProperty("classify.db.url");
        username = properties.getProperty("classify.db.username");
        password = properties.getProperty("classify.db.password");
    }

    @Override
    public List<ErrorType> queryAllErrorType() {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection(driver, url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        String sql = "SELECT * FROM exists_errortype";
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Object[] params = {};

        // 执行Query，获取结果集
        try {
            assert connection != null;
            resultSet = DBUtils.executeQuery(connection, sql, params, ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 结果集映射
        List<ErrorType> result = new ArrayList<>();
        try {
            assert resultSet != null;
            while (resultSet.next()) {
                // 将查询结果加入结果集中
                result.add(new ErrorType(resultSet.getInt("id"), resultSet.getInt("error_type"),
                        resultSet.getString("engine"), resultSet.getString("error_info"),
                        resultSet.getString("error_api"), resultSet.getInt("count")));
            }
        } catch (SQLException e) {
            System.out.println();
            e.printStackTrace();
        }

        // 关闭流
        try {
            DBUtils.close(connection, ps, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
