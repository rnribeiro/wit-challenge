package com.example.rest.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to generate a unique request ID for each incoming request.
 * The request ID is stored in the MDC (Mapped Diagnostic Context) and
 * also set in the response header for tracking purposes.
 */
@Component
public class RequestIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        // Generate a unique request ID
        String requestId = UUID.randomUUID().toString();
        // Store the request ID in the MDC (this can be done only in synchronous requests)
        MDC.put("requestId", requestId);
        // Set the request ID in the response header
        ((HttpServletResponse) response).setHeader("X-Request-ID", requestId);
        // Proceed with the filter chain
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}