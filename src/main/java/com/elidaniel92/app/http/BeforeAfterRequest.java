package com.elidaniel92.app.http;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

@Singleton
public class BeforeAfterRequest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void before(HttpServletRequest httpRequest) {
        String requestUrl = httpRequest.getRequestURL().toString();
        log.info("Request received: " + requestUrl);
    }

    public void after(HttpServletRequest httpRequest) {
        String requestUrl = httpRequest.getRequestURL().toString();
        log.info("Response completed for: " + requestUrl);
    }

}
