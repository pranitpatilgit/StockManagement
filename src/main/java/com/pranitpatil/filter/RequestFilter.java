package com.pranitpatil.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This filter sets a random request id for each request which gets appended to every log statement.
 * This is helpful in finding out the logs of specific request in case of a lot of concurrent requests to the server.
 */
@Component
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //Add unique request id for each request to display in logs
        MDC.put("requestID", String.valueOf(System.currentTimeMillis()));
        chain.doFilter(request, response);
    }
}
