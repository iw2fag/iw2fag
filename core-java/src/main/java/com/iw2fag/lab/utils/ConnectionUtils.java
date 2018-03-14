package com.iw2fag.lab.utils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ConnectionUtils {


    private ConnectionUtils() {

    }

    private static ProxyInfo proxyInfo;

    public static HttpResponse httpPost(String url, Map<String, String> headers, byte[] data) {

        HttpResponse response = null;
        try {
            response = doHttp("POST", url, null, headers, data);
        } catch (Exception e) {
        }
        return response;
    }

    public static HttpResponse httpGet(String url, Map<String, String> headers, Map<String, String> parameterMap) {

        HttpResponse response = null;
        try {
            response = doHttp("GET", url, parameterMap, headers, null);
        } catch (Exception e) {
        }
        return response;
    }

    public static HttpResponse httpsPost(String url, Map<String, String> headers, byte[] data) {

        HttpResponse response = null;
        try {
            response = doHttps("POST", url, null, headers, data);
        } catch (Exception e) {
        }
        return response;
    }

    public static HttpResponse httpsGet(String url, Map<String, String> headers, Map<String, String> parameterMap) {

        HttpResponse response = null;
        try {
            response = doHttps("GET", url, parameterMap, headers, null);
        } catch (Exception e) {
        }
        return response;
    }

    private static String prepareParam(String url, Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer("");
        if (paramMap != null && !paramMap.isEmpty()) {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append("?").append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
        }
        return url + sb.toString();
    }

    private static HttpResponse doHttp(String requestMethod, String connectionUrl, Map<String, String> parameterMap, Map<String, String> headers, byte[] data) throws IOException {
        HttpResponse response = new HttpResponse();
        URL url = new URL(prepareParam(connectionUrl, parameterMap));

        HttpURLConnection connection = (HttpURLConnection) openConnection(proxyInfo, url);
        connection.setRequestMethod(requestMethod);

        setConnectionHeaders(connection, headers);

        if (data != null && data.length > 0) {
            connection.setDoOutput(true);
            try {
                OutputStream out = connection.getOutputStream();
                out.write(data);
                out.flush();
                out.close();
            } catch (Throwable cause) {
                cause.printStackTrace();
            }
        }

        connection.setConnectTimeout(3 * 1000);
        connection.connect();

        //response code
        int responseCode = connection.getResponseCode();

        response.setResponseCode(responseCode);
        //response data
        if (200 == responseCode) {
            InputStream inputStream = connection.getInputStream();
            //header
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            response.setHeaders(headerFields);
        }

        connection.disconnect();

        return response;
    }

    private static HttpResponse doHttps(String requestMethod, String connectionUrl, Map<String, String> parameterMap, Map<String, String> headers, byte[] data) throws Exception {

        final SSLContext sc = SSLContext.getInstance("SSL");

        TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        sc.init(null, trustManagers, null);

        HttpResponse response = new HttpResponse();
        URL url = new URL(prepareParam(connectionUrl, parameterMap));

        HttpsURLConnection connection = (HttpsURLConnection) openConnection(proxyInfo, url);
        connection.setSSLSocketFactory(sc.getSocketFactory());
        connection.setHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        connection.setRequestMethod(requestMethod);

        setConnectionHeaders(connection, headers);

        if (data != null && data.length > 0) {
            connection.setDoOutput(true);
            try {
                OutputStream out = connection.getOutputStream();
                out.write(data);
                out.flush();
                out.close();
            } catch (Throwable cause) {
                cause.printStackTrace();
            }
        }

        connection.connect();

        //response code
        int responseCode = connection.getResponseCode();
        response.setResponseCode(responseCode);
        //response data
        InputStream inputStream = connection.getInputStream();

        //header
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        response.setHeaders(headerFields);

        connection.disconnect();

        return response;
    }

    private static URLConnection openConnection(final ProxyInfo proxyInfo, URL _url) throws IOException {

        Proxy proxy = null;

        if (proxyInfo != null && proxyInfo._host != null && proxyInfo._port != null && !proxyInfo._host.isEmpty() && !proxyInfo._port.isEmpty()) {

            try {
                int port = Integer.parseInt(proxyInfo._port.trim());
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyInfo._host, port));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (proxy != null && proxyInfo._userName != null && proxyInfo._password != null && !proxyInfo._password.isEmpty() && !proxyInfo._password.isEmpty()) {
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyInfo._userName, proxyInfo._password.toCharArray());
                }
            };


            Authenticator.setDefault(authenticator);
        }

        if (proxy == null) {
            return _url.openConnection();
        }


        return _url.openConnection(proxy);
    }

    private static void setConnectionHeaders(HttpURLConnection connection, Map<String, String> headers) {

        if (connection != null && headers != null && headers.size() != 0) {
            Iterator<Map.Entry<String, String>> headersIterator = headers.entrySet().iterator();
            while (headersIterator.hasNext()) {
                Map.Entry<String, String> header = headersIterator.next();
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

    }

    public static void setProxyCfg(String host, String port, String userName, String password) {

        proxyInfo = new ProxyInfo(host, port, userName, password);

    }

    public static void setProxyCfg(String host, String port) {
        proxyInfo = new ProxyInfo();
        proxyInfo._host = host;
        proxyInfo._port = port;
    }

    public static void setProxyCfg(String address, String userName, String password) {
        proxyInfo = new ProxyInfo();
        if (address != null) {
            if (address.endsWith("/")) {
                int end = address.lastIndexOf("/");
                address = address.substring(0, end);
            }

            int i = address.lastIndexOf(':');
            if (i > 0) {
                proxyInfo._host = address.substring(0, i);
                proxyInfo._port = address.substring(i + 1, address.length());
            } else {
                proxyInfo._host = address;
                proxyInfo._port = "80";
            }

            proxyInfo._userName = userName;
            proxyInfo._password = password;
        }
    }


    private static class ProxyInfo {

        private String _host;
        private String _port;
        private String _userName;
        private String _password;

        public ProxyInfo() {

        }

        public ProxyInfo(String host, String port, String userName, String password) {
            _host = host;
            _port = port;
            _userName = userName;
            _password = password;
        }

    }

}
