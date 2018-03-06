package com.iw2fag.lab.filter;

import com.iw2fag.lab.common.ServerLoggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpRequestFilterHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoggers.FILTER);

    private Map<String, Map<Pattern, String>> requestPatterns = new ConcurrentHashMap<>();
    private Map<String, Set<Pattern>> pathPatterns = new ConcurrentHashMap<>();

    void initPatterns(FilterConfig filterConfig, String initParameter, String defaultValue) throws ServletException{
        // get excluded patterns
        String patternsArgument = filterConfig.getInitParameter(initParameter);
        LOGGER.debug("init parameter {} set to {}", initParameter, patternsArgument);
        Set<Pattern> patterns = extractPatterns(patternsArgument);
        if (patterns != null) {
            pathPatterns.put(initParameter, patterns);
        } else {
            boolean parameterInitialized = false;
            if (defaultValue != null) {
                patterns = extractPatterns(defaultValue);
                if (patterns != null) {
                    LOGGER.warn("Filter was initialized with default value for init parameter {}", initParameter);
                    pathPatterns.put(initParameter, patterns);
                    parameterInitialized = true;
                }
            }
            if (!parameterInitialized) {
                LOGGER.error("Filter was not initialized correctly for init parameter {}, argument {}", initParameter, patternsArgument);
                throw new ServletException("Filter was not initialized correctly");
            }
        }
    }

    void initRequests(FilterConfig filterConfig, String initParameter, String defaultValue) throws ServletException {
        // get excluded patterns
        String requestsArgument = filterConfig.getInitParameter(initParameter);
        LOGGER.debug("init parameter {} set to {}", initParameter, requestsArgument);
        Map<Pattern, String> requests = extractRequests(requestsArgument);
        if (requests != null) {
            requestPatterns.put(initParameter, requests);
        } else {
            boolean parameterInitialized = false;
            if (defaultValue != null) {
                requests = extractRequests(defaultValue);
                if (requests != null) {
                    LOGGER.warn("Filter was initialized with default value for init parameter {}", initParameter);
                    requestPatterns.put(initParameter, requests);
                    parameterInitialized = true;
                }
            }
            if (!parameterInitialized) {
                LOGGER.error("Filter was not initialized correctly for init parameter {}, argument {}", initParameter, requestsArgument);
                throw new ServletException("Filter was not initialized correctly");
            }
        }
    }

    /**
     * Check whether the specified requestUri is an excluded one (i.e. no input validation need).
     * The specifies requestRrl is an excluded one if it ends with one of Strings
     * in the requestUris member.
     * @param servletRequest the request
     * @return true the specified request is an excluded one, false otherwise.
     */
    boolean isRequestMatch(String initParameter, HttpServletRequest servletRequest) {
        Map<Pattern, String> requests = requestPatterns.get(initParameter);
        if(requests !=null){
            String method = servletRequest.getMethod();
            // no point going over the list if there is no method
            if (method != null) {
                for (Iterator it = requests.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Pattern, String> entry = (Map.Entry<Pattern, String>) it.next();
                    // match the method first (exact match)
                    if (method.equals(entry.getValue())){
                        Pattern pattern = entry.getKey();
                        Matcher matcher = pattern.matcher(servletRequest.getPathInfo());
                        if (matcher.find()) {
                            return true;
                        }
                        matcher = pattern.matcher(servletRequest.getRequestURI());
                        if (matcher.find()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean isRequestPathMatch(String initParameter, String path){
        Set<Pattern> requests = pathPatterns.get(initParameter);
        if(requests !=null){
            for(Iterator iterator = requests.iterator(); iterator.hasNext();){
                Pattern pattern = (Pattern)iterator.next();
                Matcher matcher = pattern.matcher(path);
                if(matcher.find()){
                    return true;
                }
            }
        }
        return false;
    }

    boolean isRequestURLMatch(String initParameter, HttpServletRequest servletRequest){
        String url = servletRequest.getRequestURL().toString();
        return isRequestPathMatch(initParameter, url);
    }

    boolean isRequestURIMatch(String initParameter, HttpServletRequest servletRequest){
        String url = servletRequest.getRequestURI();
        return isRequestPathMatch(initParameter, url);
    }

    void printCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies !=null && cookies.length>0 ){
            for (Cookie tempCookie : cookies) {
                LOGGER.info("Name={} ,Domain={} ,Path={} ,age={} ,Value={}", tempCookie.getName(), tempCookie.getDomain(), tempCookie.getPath(), tempCookie.getMaxAge(), tempCookie.getValue());
            }
        }
    }

    void printIpAddress(HttpServletRequest req) {
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        LOGGER.debug("Request come from: {}", ipAddress);
    }

    /**
     * Extract strings items from a comma separated String
     * into a Set object.
     * @param requests the excluded requests string
     */
    private Map<Pattern, String> extractRequests(String requests) throws ServletException {
        if (requests != null) {
            requests = requests.trim();
            Map<Pattern, String> result = new HashMap<>();
            if (!requests.isEmpty()) {
                String[] excludedRequestsArr = requests.split(",");
                for (int i = 0; i < excludedRequestsArr.length; i++) {
                    String[] excludedRequestArr = excludedRequestsArr[i].split(";");
                    if (excludedRequestArr.length == 2) {
                        Pattern pattern = Pattern.compile(excludedRequestArr[0]);
                        result.put(pattern, excludedRequestArr[1]);
                    } else {
                        throw new ServletException("Invalid request format");
                    }
                }
                return result;
            }
        }
        return null;
    }

    /**
     * Extract strings items from a comma separated String
     * into a Set object.
     * @param patterns the patterns string
     */
    private Set<Pattern> extractPatterns(String patterns) {
        if (patterns != null) {
            patterns = patterns.trim();
            Set<Pattern> result = new HashSet<Pattern>();
            if (!patterns.isEmpty()) {
                String[] excludedRequestsArr = patterns.split(",");
                for (int i = 0; i < excludedRequestsArr.length; i++) {
                    Pattern pattern = Pattern.compile(excludedRequestsArr[i]);
                    result.add(pattern);
                }
                return result;
            }
        }
        return null;
    }

}
