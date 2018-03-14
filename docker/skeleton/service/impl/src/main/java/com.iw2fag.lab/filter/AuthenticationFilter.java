package com.iw2fag.lab.filter;

import com.iw2fag.lab.common.ServerLoggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoggers.FILTER);

    private static final String USER_INIT_PARAMETER = "userExcludedRequests";
    private HttpRequestFilterHelper requestFilterHelper;

    /**
     * Cons. for tests (package protected)
     * @param requestFilterHelper the filter helper
     */
    AuthenticationFilter(HttpRequestFilterHelper requestFilterHelper) {
        this.requestFilterHelper = requestFilterHelper;
    }

    /**
     * Default Cons.
     */
    public AuthenticationFilter() {
        this(new HttpRequestFilterHelper());
    }

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            requestFilterHelper.initRequests(filterConfig, USER_INIT_PARAMETER, "^/getAPIVersion;GET");
        } catch (Exception ignored){
            // do nothing
            LOGGER.warn("Ignored", ignored);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        if (request == null && response == null) {
            throw new ServletException();
        }

        if (!isUserAuthenticated((HttpServletRequest)request)) {
            HttpServletResponse resp = (HttpServletResponse)response;
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    /**
     * logic to accept or reject access to the page, check log in status
     * @return true when authentication is deemed valid
     */
    boolean isUserAuthenticated(HttpServletRequest req){
        if (req == null) {
            return false;
        }

        LOGGER.debug("Request Path= {}", req.getPathInfo());
        requestFilterHelper.printIpAddress(req);
        if(requestFilterHelper.isRequestMatch(USER_INIT_PARAMETER, req)) {
            return true;
        }

        // check the session
        return true;
        // the default for any execution path that is not completed
        //return false;
    }

}