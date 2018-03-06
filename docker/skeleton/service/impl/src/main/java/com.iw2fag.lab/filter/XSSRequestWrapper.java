package com.iw2fag.lab.filter;

import com.iw2fag.lab.common.ServerLoggers;
import com.iw2fag.lab.utils.StreamUtils;
import com.iw2fag.lab.security.InputValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;



public class XSSRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(ServerLoggers.FILTER);

    private Map<String, String[]> sanitizedQueryString;
    private String body=null;


    public XSSRequestWrapper(HttpServletRequest request) throws IOException{
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = StreamUtils.createInputReader(inputStream, request.getCharacterEncoding());
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        //sanitize
        if(body!=null || !body.isEmpty()){
            if(request!=null && request.getContentType()!=null) {
                if(request.getContentType().contains("application/json")){
                    //clean XSS context
                    body = InputValidationUtils.cleanParams(body);
                }

            }
        }
    }


    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public BufferedReader getReader() throws IOException {

        logger.debug("getReader called ... for path ");
        StringBuffer jb = new StringBuffer();
        BufferedReader reader = null;
        String line = null;
        try {
            reader = super.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        if(jb != null && jb.length() > 0){
            logger.debug("getReader data: " + jb.toString());
        }

        return reader;

        //return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            private ReadListener readListener;

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            public int read() throws IOException {
                //logger.debug("getInputStream data: " + body);
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String,String[]> getParameterMap() {
        if(sanitizedQueryString == null) {
            Map<String, String[]> res = new HashMap<String, String[]>();
            Map<String, String[]> originalQueryString = super.getParameterMap();
            if(originalQueryString!=null) {
                for (String key : originalQueryString.keySet()) {
                    String[] rawVals = originalQueryString.get(key);
                    String[] snzVals = new String[rawVals.length];
                    for (int i=0; i < rawVals.length; i++) {
                        snzVals[i] = InputValidationUtils.stripXSS(rawVals[i]);
                       //logger.debug("Sanitized: " + rawVals[i] + " to " + snzVals[i]);
                    }
                    res.put(InputValidationUtils.stripXSS(key), snzVals);
                }
            }
            sanitizedQueryString = res;
        }
        return sanitizedQueryString;
    }


    @Override
    public String getHeader(String name) {
        return InputValidationUtils.stripXSS(super.getHeader(name));
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = InputValidationUtils.stripXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return InputValidationUtils.stripXSS(value);
    }
}
