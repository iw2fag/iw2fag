package com.iw2fag.lab.filter;

import org.eclipse.jetty.servlets.DoSFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter will limit the number of requests per second by the same user.
 */
public class DosPerUserFilter extends DoSFilter {

    @Override
    protected String extractUserId(ServletRequest request) {
        //return userId
        return null;
    }

    @Override
    public boolean isTrackSessions()
    {
        return false;
    }

    private static final String INIT_PARAMETER = "excludePatterns";
    protected HttpRequestFilterHelper requestFilterHelper;

    DosPerUserFilter(HttpRequestFilterHelper requestFilterHelper) {
        this.requestFilterHelper = requestFilterHelper;
    }

    public DosPerUserFilter() {
        this(new HttpRequestFilterHelper());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        requestFilterHelper.initPatterns(filterConfig, INIT_PARAMETER, null);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        if (request == null && response == null) {
            throw new ServletException();
        }
        if(requestFilterHelper.isRequestURLMatch(INIT_PARAMETER, (HttpServletRequest) request)){
            // for exclude patterns requests, disable the DoS Filter for them
            filterChain.doFilter(request, response);
            return ;
        }
        super.doFilter(request, response, filterChain);
    }
}
