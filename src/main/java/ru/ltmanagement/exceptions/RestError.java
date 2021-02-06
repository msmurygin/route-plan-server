package ru.ltmanagement.exceptions;

import org.springframework.http.HttpStatus;

public class RestError implements ApiError {

    private final String message;

    private final HttpStatus status;

    public RestError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "SimpleApiError{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
