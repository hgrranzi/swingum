package com.hgrranzi.swingum.persistence;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.hgrranzi.swingum.config.ApplicationConfig.getProperty;

@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DBConnectionManager {

    public static final String DB_URL = "datasource.url";
    public static final String DB_USER = "datasource.username";
    public static final String DB_PASSWORD = "datasource.password";
    public static final String DB_FILE = "db.json";

    public static Connection open() throws SQLException {
        return java.sql.DriverManager.getConnection(getProperty(DB_URL),
                getProperty(DB_USER),
                getProperty(DB_PASSWORD));

    }

    public static File openFile() throws IOException {
        File file = new File(DB_FILE);
        if (!file.createNewFile() && !file.exists()) {
            throw new IOException("Permissions or path issues.");
        }
        if (!file.canRead() || !file.canWrite()) {
            throw new IOException("Application does not have read/write access to the database file.");
        }
        return file;
    }
}
