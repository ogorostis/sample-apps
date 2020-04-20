package com.examples.common;

public class SampleException extends RuntimeException {
    private int status = 500;

    public SampleException(final String message) {
        super(message);
    }

    public SampleException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public SampleException(final int status, final String message, final Throwable throwable) {
        super(message, throwable);
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
