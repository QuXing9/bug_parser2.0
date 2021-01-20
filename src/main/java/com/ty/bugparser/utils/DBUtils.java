package com.ty.bugparser.utils;

import java.sql.*;

public class DBUtils {

    public static Connection getConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static ResultSet executeQuery(Connection connection, String sql, Object[] params, PreparedStatement ps) throws SQLException {
        ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1, params[i]);
        }
        return ps.executeQuery();
    }

    public static int executeUpdate(Connection connection, String sql, Object[] params, PreparedStatement ps) throws SQLException {
        ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1, params[i]);
        }
        return ps.executeUpdate();
    }

    public static void close(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }

        if (ps != null) {
            ps.close();
        }

        if (con != null) {
            con.close();
        }
    }
}