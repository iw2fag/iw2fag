package com.iw2fag.lab.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailValidator {

    public static  EmailValidator instance = new EmailValidator();
    public static int MIN_EMAIL_LENGTH = 4;
    public static int MAX_EMAIL_LENGTH = 50;

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[A-Za-z]+[_A-Za-z0-9-]*(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+[_A-Za-z0-9-]*(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public static EmailValidator getInstance() {
        return instance;
    }

    public boolean validate(String email) {
        if (email==null || email.length()<MIN_EMAIL_LENGTH || email.length()>MAX_EMAIL_LENGTH){
            return false;
        }

        matcher = pattern.matcher(email);
        return matcher.matches();

    }

}
