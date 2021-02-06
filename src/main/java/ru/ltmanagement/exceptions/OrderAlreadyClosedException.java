package ru.ltmanagement.exceptions;

public class OrderAlreadyClosedException extends RuntimeException {

    public OrderAlreadyClosedException(String msg, String... prms) {
        super(String.format(msg,prms));
    }
}
