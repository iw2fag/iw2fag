package com.iw2fag.lab.logging;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.contrib.json.access.JsonLayout;

import java.util.Map;


public class SaaSJsonAccessLayout extends JsonLayout {

    protected String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected Map toJsonMap(IAccessEvent event) {
        Map map = super.toJsonMap(event);
        map.put("service", serviceName);
        map.put("loggerType", "Access");

        return map;
    }
}
