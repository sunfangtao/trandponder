package com.aioute.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

public class CodeErrorException extends CredentialsException {
    public CodeErrorException() {
    }

    public CodeErrorException(String message) {
        super(message);
    }

    public CodeErrorException(Throwable cause) {
        super(cause);
    }

    public CodeErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}