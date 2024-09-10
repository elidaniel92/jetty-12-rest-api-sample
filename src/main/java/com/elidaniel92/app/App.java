package com.elidaniel92.app;

import org.int4.dirk.api.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.ioc.Root;

public class App {
     
    private static Logger log = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {           
        log.info("Application has been started successfully");
        Injector injector = Root.setting();    
		var bootstrap = injector.getInstance(Bootstrap.class);
        bootstrap.initialize();        
    }
}
