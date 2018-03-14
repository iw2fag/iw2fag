package com.iw2fag.lab.filter;

/**
 * Created with IntelliJ IDEA.
 * User: davidofv
 * Date: 01/03/15
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

import com.iw2fag.lab.common.ServerLoggers;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This filter performs input validation and cleaning for request parameters, headers and cookies.
 * This filter should be a first filter in web.xml (or just after GZIP or decode filters if exists)
 * Usage:
 * Insert entry point interceptor in application web.xml
 * This filter should be the first (or just after DECODER/GZIP filters if exists).

    <servlet>
        <servlet-name>InputValidationFilter</servlet-name>
        <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
        <init-param>
            <param-name>com.sun.jersey.spi.container.ContainerRequestFilters</param-name>
            <param-value>com.iw2fag.lab.filter.InputValidationFilter;</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
       <servlet-name>InputValidationFilter</servlet-name>
       <url-pattern>/*</url-pattern>
     </servlet-mapping>


 */

public class InputValidationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ServerLoggers.FILTER);

    private Set excludedParam = null;
    private static final char  COMMA  = ',';


    @Override
    public void init(FilterConfig filterConfig) {
        String modulesStr = filterConfig.getInitParameter("excludePatterns");
        if(modulesStr!=null)
            this.excludedParam = this.extractExcludedItems(modulesStr);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        if (request == null && response == null) {
            throw new ServletException();
        }
        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURL().toString();
        if (isExcludedUrl(url)) {
            chain.doFilter(request, response);
            return;
        }
        if (isExcludedContentType(request)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            XSSRequestWrapper wrapper = new XSSRequestWrapper(httpServletRequest);
            chain.doFilter(wrapper, response);
        } catch (IntrusionException iex) {
            logger.error("Intrusion detected. request blocked!", iex);
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, iex.getUserMessage());
            }
        }
    }

    private boolean isExcludedContentType(ServletRequest request) {
        if(request.getContentType()!=null){
            String contentType = request.getContentType();
            //exclude multipart/octet-stream requests
            return contentType.contains("multipart/form-data") || contentType.contains("application/octet-stream");
        }
        return false;
    }

    /**
     * Extract strings items from a comma separeted String
     * into a Set object.
     * @param excludedParamsStr
     */
    private Set extractExcludedItems(String excludedParamsStr) {
        Set excludedParam = new HashSet();
        if (excludedParamsStr != null && !excludedParamsStr.equals("")) {
            excludedParamsStr = excludedParamsStr.trim();
            String[] excludedParamsArr = excludedParamsStr.split(Character.toString(COMMA));
            for (int i=0 ; i < excludedParamsArr.length ; i++) {
                excludedParam.add(excludedParamsArr[i]);
            }
        }
        return excludedParam;
    }

    /**
     * Check whether the specified requestUri is an excluded one (i.e. no input validation need).
     * The specifies requestRrl is an excluded one if it ends with one of Strings
     * in the requestUris member.
     * @param requestUri pattern
     * @return true the specified requestUri is an excluded one, false otherwise.
     */
    private boolean isExcludedUrl(String requestUri) {
        if (requestUri.endsWith("/v2/license")) {
            return true;
        }
        if(this.excludedParam!=null){
            for (Iterator it = this.excludedParam.iterator() ; it.hasNext() ; ) {
                String excludedUrlSuffix = (String)it.next();
                logger.debug("isExcludedUrl() current URI : " + requestUri);
                Pattern pattern = Pattern.compile(excludedUrlSuffix);
                Matcher matcher = pattern.matcher(requestUri);
                if (matcher.matches()) {
                    logger.debug("isExcludedUrl() found matcher : " + excludedUrlSuffix);
                    return true;
                }
            }
        }
        return false;
    }

}
