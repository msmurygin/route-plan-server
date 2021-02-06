package ru.ltmanagement.exceptions;

public class OrderInProcessException extends RuntimeException {
    public OrderInProcessException(String msg) {
        super(msg);
    }
}
