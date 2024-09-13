package com.elidaniel92.app.http;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.ioc.Root;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*") // Apply to all requests
public class FilterImpl implements Filter {
    BeforeAfterRequest beforeAfterRequest;
    protected final Logger log = LoggerFactory.getLogger(FilterImpl.class);

    public FilterImpl() {
        this.beforeAfterRequest = Root.getInjector().getInstance(BeforeAfterRequest.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            this.beforeAfterRequest.before(httpRequest);            
            chain.doFilter(request, response);
            this.beforeAfterRequest.after(httpRequest);            
        }
        chain.doFilter(request, response);
    }
}
