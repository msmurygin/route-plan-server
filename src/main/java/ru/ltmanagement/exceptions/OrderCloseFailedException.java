package ru.ltmanagement.exceptions;

public class OrderCloseFailedException extends RuntimeException {
    public OrderCloseFailedException(String message) {
        super(message);
    }
}
