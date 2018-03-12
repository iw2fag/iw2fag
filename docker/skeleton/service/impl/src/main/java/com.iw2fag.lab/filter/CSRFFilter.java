package com.iw2fag.lab.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;


public class CSRFFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSRFFilter.class);

    public static final String CSRF_COOKIE_NAME = "csrfsecret";
    public static final String CSRF_HEADER_NAME = "x-csrfsecret";
    public static final String EXCEPTION_MSG = "Cross Site Request Forgery (CSRF) Detected";
    public static final String CSRF_INIT_PARAMETER = "csrfExcludedRequests";


    private HttpRequestFilterHelper requestFilterHelper;


    @Value("${allow.csrf:false}")
    boolean allowCSRF;


    public CSRFFilter() {
        requestFilterHelper = new HttpRequestFilterHelper();
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("allowCSRF flag is {}", allowCSRF);
        try {
            requestFilterHelper.initRequests(filterConfig, CSRF_INIT_PARAMETER, null);
        } catch (Exception exp) {
            // do nothing
            LOGGER.error("error in CSRF filter", exp);
        }
    }


    boolean isCSRFRequired(HttpServletRequest httpServletRequest) {
        return allowCSRF && !requestFilterHelper.isRequestMatch(CSRF_INIT_PARAMETER, httpServletRequest);
    }

    boolean isGetMethod(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getMethod().equals(HttpMethod.GET.name()) ||
                httpServletRequest.getMethod().equals(HttpMethod.HEAD.name()) &&
                        httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name());
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest == null && servletResponse == null) {
            throw new ServletException();
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;


        boolean isGET = isGetMethod(httpServletRequest);

        boolean csrfRequired = isCSRFRequired(httpServletRequest) && !isGET;
        String csrfCookieValue = getCSRFCookieValue(httpServletRequest);
        String csrfSessionValue = getCSRFSessionValue(httpServletRequest);
        boolean cookieOrSessionValueExist = !StringUtils.isEmpty(csrfCookieValue);
        if (csrfRequired) {
            String headerValue = httpServletRequest.getHeader(CSRF_HEADER_NAME);
            cookieOrSessionValueExist = cookieOrSessionValueExist || !StringUtils.isEmpty(csrfSessionValue);
            if (cookieOrSessionValueExist) {
                // compare CSRF headerValue and cookie values - UI is responsible to add the headerValue value
                boolean csrfCookieValid = !StringUtils.isEmpty(csrfCookieValue) && csrfCookieValue.equals(headerValue);
                boolean csrfSessionValid = !StringUtils.isEmpty(csrfSessionValue) && csrfSessionValue.equals(headerValue);
                if (!csrfCookieValid && !csrfSessionValid) {
                    LOGGER.error("CSRF headerValue not found for request: {}, headerValue: [{}]", httpServletRequest.getRequestURI(), headerValue);
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, EXCEPTION_MSG);
                    return;
                }
            }

        }//else do nothing

        if (!cookieOrSessionValueExist) {
            String value = !StringUtils.isEmpty(csrfSessionValue) ? csrfSessionValue : UUID.randomUUID().toString();
            Cookie cookie = new Cookie(CSRF_COOKIE_NAME, value);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);

            LOGGER.debug("creating CSRF value   {}", value);


            httpServletRequest.getSession(true).setAttribute(CSRF_HEADER_NAME, value);//TODO remove this support old version
            httpServletResponse.addHeader(CSRF_HEADER_NAME, value);//TODO remove this support old version

        }
        //continue
        filterChain.doFilter(servletRequest, servletResponse);
    }


    //support old version
    protected String getCSRFSessionValue(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(CSRF_HEADER_NAME);
        return attribute == null ? null : (String) attribute;
    }

    protected String getCSRFCookieValue(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CSRF_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void destroy() {
    }
}
