package com.aioute.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

public class NoCodeException extends CredentialsException {
    public NoCodeException() {
    }

    public NoCodeException(String message) {
        super(message);
    }

    public NoCodeException(Throwable cause) {
        super(cause);
    }

    public NoCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}