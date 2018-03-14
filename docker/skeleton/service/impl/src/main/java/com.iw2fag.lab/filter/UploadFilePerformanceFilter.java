package com.iw2fag.lab.filter;

import com.iw2fag.lab.common.ServerLoggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class UploadFilePerformanceFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoggers.FILTER);

    private static final String MAX_CONCURRENT_UPLOADS = "maxConcurrentUploads";

    private int maxConcurrentUploads = 5;
    private final AtomicInteger concurrentUploads = new AtomicInteger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            maxConcurrentUploads =
                    Integer.parseInt(filterConfig.getInitParameter(MAX_CONCURRENT_UPLOADS));
        } catch (Exception e) {
            // Use the default threshold if the limit is not configured correctly.
            LOGGER.warn("Invalid config.", e);
        }
        concurrentUploads.set(maxConcurrentUploads);
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Currently only filter apk.
        boolean needFilter = false;
        if (request instanceof HttpServletRequest) {
            String fileName = ((HttpServletRequest) request).getHeader("filename");
            // only post method need upload filter.
            String method = ((HttpServletRequest) request).getMethod();
            needFilter = fileName != null && "POST".equalsIgnoreCase(method);
        }

        try {
            if (needFilter && concurrentUploads.decrementAndGet() < 0) {
                LOGGER.info("Max concurrent uploads exceeded.");
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
                return;
            }
            chain.doFilter(request, response);
        } finally {
            if (needFilter) {
                concurrentUploads.incrementAndGet();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
