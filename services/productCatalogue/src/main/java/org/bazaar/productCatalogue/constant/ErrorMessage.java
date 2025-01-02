package org.bazaar.productCatalogue.constant;

public class ErrorMessage {
    public final static String ID_CANNOT_BE_NULL = "You must provide a non-null id.";
    public final static String SALE_ID_NOT_FOUND = "A sale with the given id does not exist.";
    public final static String START_DATE_CANNOT_BE_AFTER_END_DATE = "Start of sale cannot be after end of sale.";
    public final static String DUPLICATE_SALE_NAME = "A sale with the given name already exists.";

    public final static String SALE_STATUS_ID_NOT_FOUND = "A sale status with the given id does not exist.";
    public final static String SALE_STATUS_STATUS_NOT_FOUND = "A sale status with the given status does not exist.";

    public final static String INVENTORY_SERVICE_CONNECTION_ERROR = "An error occured while connecting to inventory service.";

    public final static String INVALID_STATE_TRANSITION = "The sale already has the state you are trying to set it to.";
}
