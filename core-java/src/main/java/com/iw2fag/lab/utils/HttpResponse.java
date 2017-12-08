package com.iw2fag.lab.utils;

import java.util.List;
import java.util.Map;


public class HttpResponse {

    private Map<String, List<String>> headers;
    private int responseCode;
    private String responseData;

    public HttpResponse() {

    }

    public HttpResponse(Map<String, List<String>> headers, String responseData) {
        this.headers = headers;
        this.responseData = responseData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getResponseData() {
        return responseData;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "headers=" + headers +
                ", responseCode='" + responseCode + '\'' +
                ", responseData='" + responseData + '\'' +
                '}';
    }
}
