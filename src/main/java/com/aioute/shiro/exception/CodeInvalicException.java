package com.aioute.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

public class CodeInvalicException extends CredentialsException {
    public CodeInvalicException() {
    }

    public CodeInvalicException(String message) {
        super(message);
    }

    public CodeInvalicException(Throwable cause) {
        super(cause);
    }

    public CodeInvalicException(String message, Throwable cause) {
        super(message, cause);
    }
}