package org.bazaar.giza.constant;

public class ValidationMessage {
    public static final String NOT_BLANK = " cannot be empty or null";
    public static final String NOT_NULL = " cannot be null";

    public static final String INVALID_EMAIL = "Email must be valid";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static final String INVALID_PHONE_NUMBER = "Phone number must be valid";
    public static final String PHONE_NUMBER_REGEX = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
}
