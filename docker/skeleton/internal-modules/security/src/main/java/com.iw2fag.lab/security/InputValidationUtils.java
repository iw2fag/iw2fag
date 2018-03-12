package com.iw2fag.lab.security;

import com.google.json.JsonSanitizer;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.errors.ConfigurationException;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class InputValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(InputValidationUtils.class);

    /**
     * Path to the AntiSamy configuration file
     */
    private static final String ANTISAMY_ESAPI_XML = "esapi/antisamy-esapi.xml";

    /**
     * OWASP AntiSamy markup verification policy
     */
    private static Policy antiSamyPolicy = null;
    private static AntiSamy antiSamy = new AntiSamy();
    private static Encoder encoder = ESAPI.encoder();

    //load policy
    static {
        try {
            antiSamyPolicy =
                    Policy.getInstance(InputValidationUtils.class.getClassLoader().getResource(
                            ANTISAMY_ESAPI_XML));
        } catch (PolicyException e) {
            throw new ConfigurationException("Couldn't parse antisamy policy", e);
        }

        //apply deobfuscate for trust password
        if(System.getProperty("javax.net.ssl.trustStorePassword")!=null){
            String obs = System.getProperty("javax.net.ssl.trustStorePassword");
            if(obs.startsWith("OBF:")){
                String clear = org.eclipse.jetty.util.security.Password.deobfuscate(obs);
                System.setProperty("javax.net.ssl.trustStorePassword",clear);
            }
        }
    }

    /**
     *
     * @param input data for sanitizing
     * @param isJson is input data has JSON format: true for JSON, false for HTML
     * @return sanitized output (for html and json)
     * @throws SecurityException
     */
    public static String sanitize(String input, boolean isJson) throws SecurityException {

        if (input == null) {
            return null;
        }
        String result = null;
        try {

            if(isJson){
                 result = getSafeJSON(input);
            }else{
                CleanResults cr = antiSamy.scan(input, antiSamyPolicy);
                if(cr != null) {
                    result = cr.getCleanHTML();

                    // Trim trailing spaces and new lines, since Antisamy 1.4.5 adds one more newline
                    // in case the input ends with newline
                    result = result.trim();

                    if(cr.getNumberOfErrors() != 0) {
                        List errorMessages = cr.getErrorMessages();
                        for(Object errorMessage : errorMessages) {
                            logger.error("Antisamy error message: " + errorMessage.toString());
                        }
                    }
                }
            }

        } catch (PolicyException e) {
            throw new SecurityException(e);
        } catch (ScanException e) {
            throw new SecurityException(e);
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Sanitized: input [" + input + "], output [" + result + "]");
        }

        return result;
    }



    /**
     * Apply the XSS filter to the parameters       *
     */
    public static String cleanParams(String jsonValue)
    {
        if (jsonValue == null) {
            return null;
        }
        logger.debug("Checking for XSS Vulnerabilities: {}", jsonValue );

        /*
        if (isJSON(jsonValue)){
            jsonValue = getSafeJSON(jsonValue);
            return jsonValue;
        }else{
            return stripXSS(jsonValue);
        }  */


        JSONObject jsonObject;
        JSONObject fixedJson =  new JSONObject();

        try {
            if (isJSON(jsonValue)){
                jsonValue = getSafeJSON(jsonValue);
                jsonObject = new JSONObject(jsonValue);
            }else{
                return stripXSS(jsonValue);
            }

        } catch (JSONException e) {

            try {
                //If array - parse and strip XSS
                JSONArray array = new JSONArray(jsonValue);
                return jsonValue;
                /*
                JSONArray fixedArray  = new JSONArray();
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = array.getJSONObject(n);
                    fixedArray.put(cleanParams(object.toString()));
                }
                //NOTE: Very tricky! JSONArray keep slashes - so need replace here!
                return fixedArray.toString().replaceAll("\\\\", "");
                */

            } catch (JSONException ex1) {
                logger.error("cleanParams XSS Vulnerabilities for value:["+ jsonValue +"]", e);
                return jsonValue;
            }
        }

        //go through all json elements and strip XSS
        try{
            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ){
                String key = (String)keys.next();
                logger.debug("cleanParams key: {}", key);

                if(jsonObject.get(key) instanceof String){
                    String value = jsonObject.getString(key);
                    String cleanValue;
                    if (isJSON(value)){
                        cleanValue = cleanParams(value);
                    }  else {
                        cleanValue = stripXSS(value);
                    }
                    if (logger.isDebugEnabled()) {
                        if (!value.equals(cleanValue)) {
                            logger.debug("before cleanParams value: {}", value);
                            logger.debug("after cleanParams value: {}", cleanValue);
                        }
                    }
                    fixedJson.put(key,cleanValue);

                } else{
                    fixedJson.put(key,jsonObject.get(key));
                }
            }
        } catch (Exception ex){
            logger.error("cleanParams XSS Vulnerabilities for value:["+ jsonValue +"] ,  error: "+ ex.getMessage() );
            return jsonValue;
        }

        logger.debug( "XSS Vulnerabilities fixed: " +  fixedJson );

        return fixedJson.toString();
    }


    private static boolean isJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    /**
     * Strips any potential XSS threats out of the value
     * @param value the input string
     * @return clean string
     */
    public static String stripXSS(String value)
    {
        if( value == null )
            return null;

        logger.debug("XSS checking for Vulnerabilities: {}", value);

        // Use the ESAPI library to avoid encoded attacks.
        String cleanValue = getEncoder().canonicalize(value);

        // Avoid null characters
        cleanValue = cleanValue.replaceAll("\0", "");

        // Clean out HTML
        //value =  sanitize(value,false);
        cleanValue = cleanXSS(cleanValue);

        if (logger.isDebugEnabled()) {
            if (!value.equals(cleanValue)) {
                logger.debug("XSS Vulnerabilities removed: {}", cleanValue);
            }
        }

        return cleanValue;
    }

    /**
     * Clean input string from XSS vulnerabilities.
     * @param value - string for sanitize
     * @return  sanitized string
     */
    public static String cleanXSS(String value){
        if(value== null)
            return null;
        if (Jsoup.isValid(value, Whitelist.none())) {
            return value;
        }
        String clean = Jsoup.clean(value, Whitelist.none());
        return StringEscapeUtils.unescapeHtml(clean);
    }

    public static Encoder getEncoder(){
        return encoder;
    }


    /**
     * Sanitize JSON input.
     * USAGE: var myValue = eval(getSafeJSON("JSON STRING")); or  var myValue = JSON.parse(getSafeJSON("JSON STRING"));
     * @param json the input json
     * @return safe json
     */
    public static String getSafeJSON(String json) {
        try {
            return JsonSanitizer.sanitize(json);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    /**
     * Encode SQL string for prevent SQL Injection attacks.
     * @param sqlField  the text to encode for SQL
     * @return input encoded for use in SQL
     */
    public static String encodeForSQL(String sqlField){
        Codec POSTGRESS_CODEC = new OracleCodec();  //TBD We need create postgress codec? or we can use ORACLE?
        return ESAPI.encoder().encodeForSQL( POSTGRESS_CODEC, sqlField);
    }


    /**
     * Sanitize output to reports log
     * @param data the log data
     * @return clean log data
     */
    public static String sanitizeLog(String data) {
        if (data == null)
            return null;

        StringBuilder sb = new StringBuilder("");
        String[] lines = data.split(System.lineSeparator());
        List<String> newLines = new LinkedList<String>();
        for (String line : lines) {
            String currentLine = line.toLowerCase();
            if (currentLine.contains("session") ||
                    currentLine.contains("passphrase") ||
                    currentLine.contains("ldap:") ||
                    currentLine.contains("jdbc:") ||
                    currentLine.contains("creditcard") ||
                    currentLine.contains("credit_card") ||
                    currentLine.contains("credit-card") ||
                    currentLine.contains("account") ||
                    currentLine.contains("address") ||
                    currentLine.contains("password")) {
                int firstCol = currentLine.indexOf("/", 0);
                if (firstCol > 1) {
                    String prefix = currentLine.substring(0, firstCol - 1);
                    newLines.add(prefix + "[ MASKED BY SECURITY ]");
                } else {
                    newLines.add(currentLine);
                }
                newLines.add(System.lineSeparator());
            } else if (currentLine.contains("type:textchange") || currentLine.contains("actionname:settext") || currentLine.contains("firing settext")) {
                int startValue = line.indexOf("value:", 0);
                int endValue = line.length();
                if (line.indexOf(")", startValue) > 0) {
                    endValue = line.indexOf(")", startValue);
                } else if (line.indexOf("]", startValue) > 0) {
                    endValue = line.indexOf("]", startValue);
                }

                //{"value":"fff yhg gg"}
                if (startValue > 1) {
                    String prefix = line.substring(0, startValue - 1);
                    if (endValue >= 0 && endValue != line.length()) {
                        String postfix = line.substring(endValue + 1, line.length());
                        newLines.add(prefix + "value:******" + postfix);
                    } else {
                        newLines.add(prefix + " value:******");
                    }
                }else{
                    newLines.add(line);
                }
                newLines.add(System.lineSeparator());
            } else {
                newLines.add(line);
                newLines.add(System.lineSeparator());
            }
        }
        for (int i = 0; i < newLines.size(); i++) {
            sb = sb.append(newLines.get(i));
        }
        return sb.toString();

    }

}
