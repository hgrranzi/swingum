package com.hgrranzi.swingum.persistence;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;

import static com.hgrranzi.swingum.config.ApplicationConfig.getProperty;

@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DBConnectionManager {

    public static final String DB_URL = "datasource.url";
    public static final String DB_USER = "datasource.username";
    public static final String DB_PASSWORD = "datasource.password";

    public static Connection open() {
        try {
            return java.sql.DriverManager.getConnection(getProperty(DB_URL),
                                                        getProperty(DB_USER),
                                                        getProperty(DB_PASSWORD));
        } catch (Exception e) {
            //todo: if no database connection, use file storage or do not load/save game
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }
}
