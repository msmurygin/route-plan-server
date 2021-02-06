package ru.ltmanagement.exceptions;

public class OrderCanNotBeClosedException extends RuntimeException {
    public OrderCanNotBeClosedException(){
        super();
    }

    public OrderCanNotBeClosedException(String message){
        super(message);
    }
}
