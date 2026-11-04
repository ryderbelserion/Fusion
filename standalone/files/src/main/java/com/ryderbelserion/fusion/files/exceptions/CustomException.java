package com.ryderbelserion.fusion.files.exceptions;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class CustomException extends IllegalStateException {

    public CustomException(final String message, final Exception exception) {
        super(message, exception);
    }

    public CustomException(final String message) {
        super(message);
    }
}