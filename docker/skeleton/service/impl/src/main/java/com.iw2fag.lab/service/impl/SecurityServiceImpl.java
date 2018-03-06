package com.iw2fag.lab.service.impl;

import com.iw2fag.lab.service.RestService;
import com.iw2fag.lab.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final String address = System.getenv("SECURITY_SERVICE_ADDR");
    private final String port = System.getenv("SECURITY_SERVICE_PORT");

    @Autowired
    RestService restService;

    @Override
    public boolean validateUser(String token) {
        try {
            return "OK".equals(restService.get(buildUrl("validateUser") + "?token=" + token));
        } catch (Exception ex) {
            logger.error("Failed to verify user", ex);
        }
        return false;
    }

    private String buildUrl(String functionName) {
        return "http://" + address + ":" + port + "/security-service/" + functionName;
    }
}
