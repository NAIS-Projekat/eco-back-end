package com.eco.environet.users.exception;

public class EmailNotSentException extends RuntimeException {

    public EmailNotSentException(final String message) { super(message); }
}