package ru.ltmanagement.exceptions;

public class ReleaseFailedException extends RuntimeException{

    public ReleaseFailedException(String msg, String... prms) {
        super(String.format(msg,prms));
    }

    public ReleaseFailedException(String msg) {
        super(msg);
    }

}
