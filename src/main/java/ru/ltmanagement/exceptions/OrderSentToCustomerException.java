package ru.ltmanagement.exceptions;

public class OrderSentToCustomerException extends RuntimeException {

    public OrderSentToCustomerException(String msg, String... prms) {
        super(String.format(msg,prms));
    }
    public OrderSentToCustomerException(String msg) {
        super(msg);
    }
}
