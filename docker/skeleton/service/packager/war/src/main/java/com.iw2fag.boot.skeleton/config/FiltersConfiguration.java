package com.iw2fag.boot.skeleton.config;

import com.iw2fag.lab.filter.*;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class FiltersConfiguration {

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean authenticationFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("userExcludedRequests", "^/getAPIVersion;GET");
        registration.setAsyncSupported(true);
        registration.setName("authenticationFilter");
        return registration;
    }

    @Bean
    public DosPerUserFilter dosPerUserFilter() {
        return new DosPerUserFilter();
    }

    @Bean
    public FilterRegistrationBean dosPerUserFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(dosPerUserFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("maxRequestsPerSec", "100");
        registration.addInitParameter("maxRequestMs", "300000");
//        registration.addInitParameter("delayMs", "-1");
        registration.addInitParameter("excludePatterns", "^/getAPIVersion;GET");
        registration.setAsyncSupported(true);
        registration.setName("dosPerUserFilter");
        registration.setEnabled(true);
        return registration;
    }

    @Bean
    public CSRFFilter csrfFilter() {
        return new CSRFFilter();
    }

    @Bean
    public FilterRegistrationBean csrfFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(csrfFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("csrfExcludedRequests", "^/getAPIVersion;GET");
        registration.setAsyncSupported(true);
        registration.setName("csrfFilter");
        return registration;
    }

    @Bean
    public InputValidationFilter inputValidationFilter() {
        return new InputValidationFilter();
    }

    @Bean
    public FilterRegistrationBean inputValidationFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(inputValidationFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("excludePatterns", "^/getAPIVersion;GET");
        registration.setName("inputValidationFilter");
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean
    public UploadFilePerformanceFilter uploadFilePerformanceFilter() {
        return new UploadFilePerformanceFilter();
    }

    @Bean
    public FilterRegistrationBean uploadFilePerformanceFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(uploadFilePerformanceFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("maxConcurrentUploads", "5");
        registration.setAsyncSupported(true);
        registration.setName("uploadFilePerformanceFilter");
        return registration;
    }
}
