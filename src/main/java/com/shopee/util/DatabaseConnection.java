package com.shopee.util;

// import java.sql.DatabaseMetaData;
import java.sql.Connection;

public class DatabaseConnection {
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    private Connection connection;

    private DatabaseConnection() {
        this.connection = null;
    }

    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}
