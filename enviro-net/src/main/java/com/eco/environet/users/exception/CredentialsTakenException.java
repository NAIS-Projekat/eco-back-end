package com.eco.environet.users.exception;

public class CredentialsTakenException extends RuntimeException {
    public CredentialsTakenException(String message) {
        super(message);
    }
}
