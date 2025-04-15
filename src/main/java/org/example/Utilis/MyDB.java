package org.example.Utilis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {
    private static final String URL = "jdbc:mysql://localhost:3306/ecodon";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Replace with your password if needed

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connected to database 'ecodon'");
            } catch (SQLException e) {
                System.out.println("❌ Failed to connect to database: " + e.getMessage());
            }
        }
        return connection;
    }
}
