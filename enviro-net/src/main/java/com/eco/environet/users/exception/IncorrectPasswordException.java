package com.eco.environet.users.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(final String message) {
        super(message);
    }
}