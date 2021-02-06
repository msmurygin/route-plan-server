package ru.ltmanagement.exceptions;

public class RouteCanNotBeClosedException extends RuntimeException {
    public RouteCanNotBeClosedException(){
        super();
    }

    public RouteCanNotBeClosedException(String message){
        super(message);
    }
}
