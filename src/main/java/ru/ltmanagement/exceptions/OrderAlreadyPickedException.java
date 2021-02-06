package ru.ltmanagement.exceptions;

public class OrderAlreadyPickedException extends RuntimeException {


    public OrderAlreadyPickedException(String msg, String... prms) {
        super(String.format(msg,prms));
    }
    public OrderAlreadyPickedException(String msg) {
        super(msg);
    }

}
