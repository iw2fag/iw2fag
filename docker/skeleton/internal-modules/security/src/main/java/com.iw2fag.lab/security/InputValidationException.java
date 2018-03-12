package com.iw2fag.lab.security;

import org.owasp.esapi.errors.ValidationException;


public class InputValidationException extends ValidationException {

    private static final long	serialVersionUID = -8979577652909929479L;


    public InputValidationException(String userMessage, String logMessage) {
        super(userMessage, logMessage);
    }

    public InputValidationException(String userMessage) {
        super(userMessage, userMessage);
    }

    public InputValidationException(String userMessage, String logMessage, Throwable cause) {
        super(userMessage, logMessage, cause);
    }

    public InputValidationException(String userMessage, String logMessage, String context) {
        super(userMessage, logMessage ,context);
    }

    public InputValidationException(String userMessage, String logMessage, Throwable cause, String context) {
        super(userMessage, logMessage,cause, context);
    }

}
