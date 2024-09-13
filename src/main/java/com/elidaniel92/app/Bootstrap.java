package com.elidaniel92.app;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.database.DatabaseConnection;
import com.elidaniel92.app.http.HTTPServer;

@Singleton
public class Bootstrap {
    private DatabaseConnection databaseConnection;
    private HTTPServer httpServer;
    private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

    @Inject
    public Bootstrap(DatabaseConnection databaseConnection, HTTPServer httpServer) {
        this.databaseConnection = databaseConnection;
        this.httpServer = httpServer;
    }

    public void initialize() {
        log.info("Initializing all services...");

        try {
            databaseConnection.connect();            
        } catch (SQLException e) {
            log.error("Database connection failed, finishing application", e);
            return;
        }

        int port = Integer.parseInt(System.getenv("HTTP_SERVER_PORT"));
        
        try {            
            httpServer.start(port);
        } catch (Exception e) {
            log.error("Failed to start HTTP server at port " + port, e);
            return;
        }
    }

    public void destroy() {
        log.info("Destroying all services...");
        try {
            httpServer.stop();
            log.info("HTTP server stopped successfully");
        } catch (Exception e) {
            log.error("Failed to stop HTTP server", e);
        }
        try {
            databaseConnection.close();
            log.info("Database connection closed successfully");
        } catch (SQLException e) {
            log.error("Failed to close database connection", e);
        }
    }
}
