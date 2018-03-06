package com.iw2fag.lab.service.impl;

import com.iw2fag.lab.service.RestService;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class RestServiceImpl implements RestService {

    private final Logger logger = LoggerFactory.getLogger(RestServiceImpl.class);

    @Override
    public String get(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            logger.info("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    return "unknown";
                }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            logger.info("response body:" + responseBody);
            return responseBody;
        } finally {
            httpclient.close();
        }

    }
}
