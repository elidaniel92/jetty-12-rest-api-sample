package com.elidaniel92.app.http;

import java.util.EnumSet;
import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.CrossOriginHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.product.ProductController;

import jakarta.servlet.DispatcherType;

@Singleton
public class HTTPServer {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public HTTPServer() {
    }

    public void start(int port) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Add the CrossOriginHandler to protect from CSRF attacks.
        CrossOriginHandler crossOriginHandler = new CrossOriginHandler();
        crossOriginHandler.setAllowedOriginPatterns(Set.of("http://domain.com"));
        crossOriginHandler.setAllowCredentials(true);
        server.setHandler(crossOriginHandler);

        // Create a ServletContextHandler with contextPath.
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/api");
        
        context.addFilter(FilterImpl.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        
        // Link the context to the server.
        crossOriginHandler.setHandler(context);

        // Add the Servlet implementing the cart functionality to the context.
        ServletHolder servletHolder = context.addServlet(ProductController.class, "/products/*");
        // Configure the Servlet with init-parameters.
        servletHolder.setInitParameter("maxItems", "128");

        server.start();
        log.info("Server started successfully on port " + port);
    }
}
