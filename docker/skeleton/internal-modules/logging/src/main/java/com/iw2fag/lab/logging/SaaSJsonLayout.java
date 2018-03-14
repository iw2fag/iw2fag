package com.iw2fag.lab.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.Map;


public class SaaSJsonLayout extends JsonLayout {

    protected String serviceName;

    protected String loggerType;

    public String getLoggerType() {
        return loggerType;
    }

    public void setLoggerType(String loggerType) {
        this.loggerType = loggerType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        map.put("service", serviceName);
        map.put("loggerType", loggerType);
    }

    @Override
    public void addMap(String key, boolean field, Map<String, ?> mapValue, Map<String, Object> map) {
        if (field && mapValue != null && !mapValue.isEmpty()) {
            map.putAll(mapValue);
        }
    }
}
