package com.jfxbase.impl.exceptions;

import com.jfxbase.base.exceptions.ISoftException;

public class SoftRuntimeException extends RuntimeException implements ISoftException {

    public SoftRuntimeException() {
    }

    public SoftRuntimeException(String message) {
        super(message);
    }

    public SoftRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoftRuntimeException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public SoftRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
