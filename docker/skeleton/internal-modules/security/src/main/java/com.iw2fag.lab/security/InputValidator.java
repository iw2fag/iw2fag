package com.iw2fag.lab.security;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.regex.Pattern;

public final class InputValidator {

    private static final Logger logger = LoggerFactory.getLogger(InputValidator.class);

    private static final Pattern suspiciousSqlPattern =
            Pattern.compile(
                    "[\\s]*;[\\s]*|[\\s]*UNION[\\s]|[\\s]*/\\*[\\s]|[\\s]*\\*\\[\\s]|[\\s]*\r[\\s]*|[\\s]*\n[\\s]*",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern sqlVerbsPattern = Pattern.compile(
            "[\\s]*SELECT[\\s]*|[\\s]*UPDATE[\\s]|[\\s]*INSERT[\\s]|[\\s]*DELETE[\\s]|[\\s]*CREATE[\\s]|"
                    + "[\\s]*ALTER[\\s]|[\\s]*DROP[\\s]|[\\s]*JOIN[\\s]",
            Pattern.CASE_INSENSITIVE);


    enum InputValidationMode {
        OFF, LOG, ON
    }

    /**
     * Default value.
     * TBD If we need to have an option to turn it off?
     */
    private static InputValidationMode inputValidationMode = InputValidationMode.ON;

    /**
     * Checks input data length and throws exception when data is too big
     *
     * @param context
     *            the name of the field being checked (used for logging errors)
     * @param input
     *            input data
     * @param maxLength
     *            maximum allowed length
     * @throws ValidationException
     */
    public static void validateDataLength(String context, String input, int maxLength)
            throws ValidationException {
        if (!isValidData(input, maxLength)) {
            //TBD L10N message insertion
            String msg = "";
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg, msg);
            }
        }
    }



    /**
     * Checks input data length and throws exception when data is too big
     *
     * @param context
     *            the name of the field being checked (used for logging errors)
     * @param input
     *            input data
     * @param maxLength
     *            maximum allowed length
     * @throws ValidationException
     */
    public static void validateDataLength(String context, byte[] input, int maxLength)
            throws ValidationException {
        if (!isValidData(input, maxLength)) {
            //TBD L10N message insertion
            String msg = "";
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg, msg);
            }
        }
    }

    /**
     * Checks input data length
     *
     * @param input
     *            input data
     * @param maxLength
     *            maximum allowed length
     * @return true when input is valid
     */
    public static boolean isValidData(byte[] input, int maxLength) {
        return (inputValidationMode != InputValidationMode.ON || input == null || input.length <= maxLength);
    }

    /**
     * Validates date according to the format
     *
     * @param context
     *            the name of the field being checked (used for logging errors)
     * @param input
     *            input string. Can be null.
     * @param format
     *            date format
     * @param allowNull
     *            true when null is valid input
     * @throws ValidationException
     */
    public static void validateDate(
            String context,
            String input,
            DateFormat format,
            boolean allowNull) throws ValidationException {
        if (!isValidDate(context, input, format, allowNull)) {

            String msg = "Error then validate DATE : The date format is not valid(" + input +"),  format: " + format;
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg);
            }
        }
    }

    /**
     * Checks that date is valid according to the format.
     *
     * @param input
     *            input string. Can be null.
     * @param format
     *            date format
     * @param allowNull
     *            true when null is valid input
     * @return true when input is valid
     */
    public static boolean isValidDate(String context, String input, DateFormat format, boolean allowNull) {
        if (inputValidationMode != InputValidationMode.ON) {
            return true;
        }

        //field/parameter check
        context = (context == null?"":context);

        try {
            return ESAPI.validator().isValidDate(context, input, format, allowNull, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Validates integer
     *
     * @param context
     *            the name of the field being checked (used for logging errors)
     * @param input
     *            input string. Can be null.
     * @param minValue
     *            minimum allowed value
     * @param maxValue
     *            maximum allowed value
     * @param allowNull
     *            true when null is valid input
     * @throws ValidationException
     */
    public static void validateInteger(
            String context,
            String input,
            int minValue,
            int maxValue,
            boolean allowNull) throws InputValidationException {
        if (!isValidInteger(context, input, minValue, maxValue, allowNull)) {
            //TBD L10N message insertion
            String msg = "Error then validate INTEGER: (" + input +")";
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg);
            }
        }
    }

    /**
     * Checks that integer is valid
     *
     * @param input
     *            input string. Can be null.
     * @param minValue
     *            minimum allowed value
     * @param maxValue
     *            maximum allowed value
     * @param allowNull
     *            true when null is valid input
     * @return true when input is valid
     */
    public static boolean isValidInteger(String context, String input, int minValue, int maxValue, boolean allowNull) {
        if (inputValidationMode != InputValidationMode.ON) {
            return true;
        }

        //field/parameter check
        context = (context == null?"":context);

        if(input == null || input.isEmpty()){
            return false;
        }

        try {
            return ESAPI.validator().isValidInteger(context, input, minValue, maxValue, allowNull, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Performs shallow validation of SQL statement for suspicious content (;, comments, UNION) This
     * method DOES NOT provide full featured SQL parsing and validation. It should be used for
     * simple SQL queries generated from client input and should not be used for queries completely
     * generated on server side. For more safety please use prepared statements.
     *
     * @param sql
     *            SQL statement to validate
     * @throws ValidationException
     */
    public static void validateSimpleSQL(String sql) throws InputValidationException {
        if (inputValidationMode == InputValidationMode.OFF
                || sql == null
                || sql.length() == 0
                || !suspiciousSqlPattern.matcher(sql).find()) {
            return;
        }

        //TBD L10N message insertion
        String msg = "The input SQL ("+ sql +") is not valid";
        if (inputValidationMode == InputValidationMode.ON) {
            throw new InputValidationException(msg);
        }
    }

    /**
     * Performs validation of partial SQL statement to make sure that it doesn't contain SQL verbs
     * like SELECT, UPDATE, etc. Use it for input data from client as it should not contain full SQL
     * queries.
     *
     * @param sql
     *            SQL statement to validate
     * @throws ValidationException
     */
    public static void validatePartialSQL(String sql) throws InputValidationException {
        if (inputValidationMode == InputValidationMode.OFF
                || sql == null
                || sql.length() == 0
                || !sqlVerbsPattern.matcher(sql).find()) {
            return;
        }

        //TBD L10N message insertion
        String msg = "The partial SQL input ("+ sql +") is not valid";
        if (inputValidationMode == InputValidationMode.ON) {
            throw new InputValidationException(msg, msg);
        }
    }

    /**
     *
     * @param context  A descriptive name of the parameter that you are validating (e.g., LoginPage_UsernameField). This value is used by any logging or error handling that is done with respect to the value passed in.
     * @param input   The actual user input data to validate.
     * @param type   The regular expression name that maps to the actual regular expression from "ESAPI.properties".
     * @param maxLength   The maximum post-canonicalized String length allowed.
     * @param allowNull   If allowNull is true then an input that is NULL or an empty string will be legal. If allowNull is false then NULL or an empty String will throw a ValidationException.
     * @throws ValidationException
     */
    public static void validateInputField( String context,
                                           String input,
                                           String type,
                                           int maxLength,
                                           boolean allowNull) throws InputValidationException {
        if (!isValidInput(context, input, type, maxLength, allowNull)) {
            //TBD L10N message insertion
            String msg = "Input string is not valid for [" + input +"], type: [" + type +"]";
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg);
            }
        }

    }

    /**
     *   Common input validation.
     * @param context  A descriptive name of the parameter that you are validating (e.g., LoginPage_UsernameField). This value is used by any logging or error handling that is done with respect to the value passed in.
     * @param input   The actual user input data to validate.
     * @param type   The regular expression name that maps to the actual regular expression from "ESAPI.properties".
     * @param maxLength   The maximum post-canonicalized String length allowed.
     * @param allowNull   If allowNull is true then an input that is NULL or an empty string will be legal. If allowNull is false then NULL or an empty String will throw a ValidationException.
     * @return  TRUE if input passed validation FALSE if not.
     */
    public static boolean isValidInput( String context,
                                        String input,
                                        String type,
                                        int maxLength,
                                        boolean allowNull) {
        if (inputValidationMode != InputValidationMode.ON) {
            return true;
        }

        try {
            input = InputValidationUtils.getEncoder().canonicalize(input);
            return ESAPI.validator().isValidInput(context, input, type, maxLength, allowNull);
        } catch (Exception e) {
            logger.error("isValidInput Failed" ,e);
            return false;
        }
    }

    /**
     * Checks input data length
     *
     * @param input
     *            input data
     * @param maxLength
     *            maximum allowed length
     * @return true when input is valid
     */
    public static boolean isValidData(String input, int maxLength) {
        return (inputValidationMode != InputValidationMode.ON || input == null || input.length() <= maxLength);
    }

    /**
     * Perform validation of uploaded file name.
     *
     * @param context request filed name (uses for auditing)
     * @param fileName file name
     * @param isNull If allowNull is true then an input that is NULL or an empty string will be legal. If allowNull is false then NULL or an empty String will throw a ValidationException.
     * @return isValid TRU or FALSE
     */
    public static boolean validateFileName(String context, String fileName, boolean isNull ) {

        if (inputValidationMode != InputValidationMode.ON) {
            return true;
        }

        if(fileName==null || fileName.isEmpty()){
            return false;
        }

        //field/parameter check
        context = (context == null?"":context);

        try {
            return ESAPI.validator().isValidFileName(context, fileName.toLowerCase(),ESAPI.securityConfiguration().getAllowedFileExtensions(),isNull);
        } catch (IntrusionException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     *  Perform email validation.
     * @param context
     * @param input
     * @param allowNull
     * @return TRUE if email is valid.
     */
    public static boolean isValidEmail( String context,
                                        String input,
                                        boolean allowNull) {
        return isValidInput(context, input, "Email", 50, allowNull);
    }

    /**
     *  Perform password validation.
     *
     *  (?=.*\d)		#   must contains one digit from 0-9
     *  (?=.*[a-z])		#   must contains one lowercase characters
     *  (?=.*[A-Z])		#   must contains one uppercase characters
     *  (?=.*[@#$%])	#   must contains one special symbols in the list "@#$%"
     *  .	            #   match anything with previous condition checking
     *  {6,20}          #   length at least 6 characters and maximum of 20
     * @param context context
     * @param input input
     * @param allowNull allow null or not
     * @return TRUE if password is valid.
     */
    public static boolean isValidPassword( String context,
                                        String input,
                                        boolean allowNull) {
        return isValidInput(context, input, "Password", 21, allowNull);
    }

    /**
     *  Check string safety (uses for JSON attributes validation)
     * @param context
     * @param input
     * @param allowNull
     * @return  TRUE if string is safe
     */
    public static boolean isSafeString( String context,
                                        String input,
                                        boolean allowNull) {
        return isValidInput(context, input, "SafeString", 255, allowNull);
    }

    /**
     *  Check URL string
     * @param context
     * @param input
     * @param allowNull
     * @return  TRUE if string is safe
     */
    public static boolean isValidURL( String context,
                                        String input,
                                        boolean allowNull) {
        return isValidInput(context, input, "URL", 50, allowNull);
    }

    /**
     *  Check URL string
     * @param context
     * @param input
     * @param allowNull
     * @return  TRUE if string is safe
     */
    public static boolean isValidIP( String context,
                                      String input,
                                      boolean allowNull) {
        return isValidInput(context, input, "IPAddress", 15, allowNull);
    }

    /**
     * Validates double
     *
     * @param context
     *            the name of the field being checked (used for logging errors)
     * @param input
     *            input string. Can be null.
     * @param minValue
     *            minimum allowed value
     * @param maxValue
     *            maximum allowed value
     * @param allowNull
     *            true when null is valid input
     * @throws ValidationException
     */
    public static void validateDouble(
            String context,
            String input,
            double minValue,
            double maxValue,
            boolean allowNull) throws InputValidationException {
        if (!isValidDouble(input, minValue, maxValue, allowNull)) {
            //TBD L10N message insertion
            String msg = "Error then validate DOUBLE: (" + input +")";
            if (inputValidationMode == InputValidationMode.ON) {
                throw new InputValidationException(msg);
            }
        }
    }

    /**
     * Checks that double is valid
     *
     * @param input
     *            input string. Can be null.
     * @param minValue
     *            minimum allowed value
     * @param maxValue
     *            maximum allowed value
     * @param allowNull
     *            true when null is valid input
     * @return true when input is valid
     */
    public static boolean isValidDouble(
            String input,
            double minValue,
            double maxValue,
            boolean allowNull) {
        if (inputValidationMode != InputValidationMode.ON) {
            return true;
        }

        try {
            return ESAPI.validator().isValidDouble("Double", input, minValue, maxValue, allowNull, null);
        } catch (Exception e) {
            logger.error("isValidDouble Failed", e);
        }
        return false;
    }
}
