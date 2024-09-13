package com.elidaniel92.app.ioc;

import java.util.Arrays;

import org.int4.dirk.api.Injector;
import org.int4.dirk.jsr330.Injectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.Bootstrap;
import com.elidaniel92.app.database.DatabaseConnection;
import com.elidaniel92.app.product.ProductController;
import com.elidaniel92.app.product.ProductDAO;
import com.elidaniel92.app.http.BeforeAfterRequest;
import com.elidaniel92.app.http.HTTPServer;

public class Root {
    private static Logger log = LoggerFactory.getLogger(Root.class);
    private static Injector injector = null;

    public static Injector getInjector() {
        if(injector == null) {
            log.error("Injector is not initialized");
            throw new IllegalStateException("Injector is not initialized");
        }
        return Root.injector;
    }

    public static Injector setting() {
        // Configure dirk injector
		//  - there are different 'Injectors' classes per each style (jsr-330 being used here)
        log.info("Setting dirk injector...");
		Injector injector = Injectors.autoDiscovering();
		injector.register(Arrays.asList(
            DatabaseConnection.class,
            ProductDAO.class,
            ProductController.class,
            HTTPServer.class,
            BeforeAfterRequest.class,
            Bootstrap.class
        ));
        Root.injector = injector;
        return injector;        
    }   
}
