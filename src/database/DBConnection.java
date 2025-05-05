package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/personalfinancedb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "admin";
    private static final String PASSWORD = "Pratham@123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading MySQL Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
