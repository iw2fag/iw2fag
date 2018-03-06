package com.iw2fag.lab.security.encryption;

import com.iw2fag.lab.utils.FileUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public abstract class CipherUtil {
    public static final String SEP = File.separator;

    private static final Logger logger = LoggerFactory.getLogger(CipherUtil.class);

    private static final String ALGO = "AES";
    private static final String defaultKeyValue = "hp4mserverkeyhp4";

    private static final String encodedPrefix = "ENC(";
    private static final String encodedPostfix = ")";

    public static final String ENCRYPTED_PROPERTIES = "encrypted.properties";
    private static final String ENCRYPTED_KEY = "ENCRYPTED_KEY";
    public static final String CONFIGURATION_FOLDER = "conf";

    /**
     * To be redefined for setting appropriate property home path
     * @return
     */
    public abstract String getPropertyFilePath();

    private Key generateKey(String keyValue) throws Exception {
        // current ALGO is limited for first 16 chars
        if (keyValue.length()<=16){
            return new SecretKeySpec(keyValue.getBytes(), ALGO);
        }else{
            return new SecretKeySpec(keyValue.substring(0,16).getBytes(),ALGO);
        }
    }

    private String getKeyValue() {
        String keyValue = FileUtils.getKeyValue(getPropertyFilePath() + SEP + ENCRYPTED_PROPERTIES, ENCRYPTED_KEY);
        if (keyValue != null && !keyValue.isEmpty()){
            return keyValue;
        }
        else {
            logger.info("### Use default keyValue for encrypt, encrypted.properties file does not exist or is empty.");
            return defaultKeyValue;
        }
    }

    public String encode(String data) throws Exception {
        return encode(data, null);
    }

    public String encode(String data, String key) throws Exception {
        if (data == null) {
            logger.debug("Cannot encode NULL");
            return null;
        }
        return encodedPrefix + encrypt(data, key) + encodedPostfix;
    }

    public String decode(String data) {
        if (data == null) {
            logger.debug("Cannot decode NULL");
            return null;
        }
        try {
            return decode(data, null);
        } catch (Exception e) {
            logger.error("Failed to decode data!", e);
        }
        return null;
    }

    public String decode(String data, String key) throws Exception {
        if (data == null) {
            logger.debug("Cannot decode NULL");
            return null;
        }
        if (data.startsWith(encodedPrefix) && data.endsWith(encodedPostfix)) {
            return decrypt(data.substring(encodedPrefix.length(), data.length() - encodedPostfix.length()), key);
        } else {
            return data;
        }
    }

    public String encrypt(String data) throws Exception {

        return encrypt(data, null);
    }

    public String encrypt(String data, String secret) throws Exception {

        if (data == null) {
            logger.debug("Cannot encrypt NULL");
            return null;
        }
        // we avoid processing empty strings
        if (data.isEmpty()) {
            return data;
        }

        String keyValue = null;
        if(secret!=null && !secret.isEmpty()) {

            keyValue = secret;
        }else{
            keyValue = getKeyValue();
        }

        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue =new BASE64Encoder().encode(encVal);

        return encryptedValue;
    }


    public void saveEncryptedValue(String value){
        String content = ENCRYPTED_KEY + "=" + value;
        boolean result = FileUtils.saveInputToFile(content, getPropertyFilePath(), ENCRYPTED_PROPERTIES);
        if (result){
            System.out.println("### File created: " + getPropertyFilePath() + SEP +ENCRYPTED_PROPERTIES);
        }
    }

    public String decrypt(String encryptedData) throws Exception {
        return decrypt(encryptedData, null);
    }

    public String decrypt(String encryptedData, String secret) throws Exception {
        if (encryptedData == null) {
            logger.debug("Cannot decrypt NULL");
            return null;
        }
        // we avoid processing empty strings
        if (encryptedData.isEmpty()) {
            return encryptedData;
        }
        String keyValue = null;
        if(secret!=null && !secret.isEmpty()) {
            keyValue = secret;
        }else{
            keyValue = getKeyValue();
        }

        if (keyValue == null) {
            logger.debug("Cannot decrypt without keyValue.");
            return null;
        }
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new Base64().decode(encryptedData.getBytes());
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public String hashPassword(String salt, String unecryptedPassword) {
        if (salt == null || unecryptedPassword == null) {
            logger.debug("Cannot hash NULL");
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((unecryptedPassword + salt).getBytes());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot hash", e);
        }
        String encryptedPassword = (new BigInteger(messageDigest.digest())).toString(16);
        return encryptedPassword;
    }

    public String getSecuredRandomString(){
        return UUID.randomUUID().toString();
    }

}