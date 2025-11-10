package com.tennis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionUtil {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:mem:db","sa","");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
