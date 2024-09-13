package com.elidaniel92.app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DatabaseConnection {
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private String databaseServerAddress;
    private String databaseName;
    private String user;
    private String password;
    private Connection conn;
    protected final Logger log = LoggerFactory.getLogger(getClass());


    public DatabaseConnection() throws ClassNotFoundException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            log.error("Failed to load driver " + driverName, e);
            throw e;
        }

        databaseServerAddress = System.getenv("DATABASE_SERVER_ADDRES");
        databaseName = System.getenv("DATABASE_NAME");
        user = System.getenv("DATABASE_USER");
        password = System.getenv("DATABASE_PASSWORD");
    }

    public void connect() throws SQLException {
        String url = "jdbc:mysql://" + databaseServerAddress + "/" + databaseName;    
        log.info("Try to connect to database " + url + " with user " + user);
        try {
            this.conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {            
            log.error("Fail to connect to database, check the environment variables: " +
            "DATABASE_SERVER_ADDRES, " +
            "DATABASE_NAME, " +
            "DATABASE_USER and " +
            "DATABASE_PASSWORD", e);
            throw e;
        }
    }

    public void close() throws SQLException {
        this.conn.close();
    }

    public Connection getConnection() {
        return this.conn;
    }
}
