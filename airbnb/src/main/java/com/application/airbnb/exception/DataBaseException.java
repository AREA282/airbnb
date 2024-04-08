package com.application.airbnb.exception;

public class DataBaseException extends RuntimeException{

    public DataBaseException() {
    }

    public DataBaseException(String message) {
        super(message);
    }
}
