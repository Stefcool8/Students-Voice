package org.example.studentsvoice.filter;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.studentsvoice.cdi.producer.MyLogger;

import java.io.IOException;
import java.util.logging.Logger;

@WebFilter("/*")
public class CorsFilter implements Filter {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOGGER.info("CORS filter executing.");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = getHttpServletResponse((HttpServletResponse) response);

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            // Preflight request handling
            LOGGER.info("Preflight request detected.");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static HttpServletResponse getHttpServletResponse(HttpServletResponse response) {
        // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        return response;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialize filter if needed
    }

    @Override
    public void destroy() {
        // Cleanup filter resources if needed
    }
}

