package org.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/push_down_db";
        String user = "postgres";
        String password = "123456789";
        return DriverManager.getConnection(url, user, password);
    }
}
