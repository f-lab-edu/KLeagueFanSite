package com.kleague.kleaguefinder.exception;

import lombok.Getter;

@Getter
public abstract class MainException extends RuntimeException{

    public MainException(String type, String message, String field) {
        super(makeMessage(type, message, field));
    }

    public MainException(String type, String message, String field, Throwable cause) {
        super(makeMessage(type, message, field), cause);
    }

    public abstract int getStatusCode();

    private static String makeMessage(String type, String message, String field) {
        return ("[" + type + "] " + message + " { 필드 : " + field + " }" );
    }
}
