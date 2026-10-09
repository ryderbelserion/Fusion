package com.ryderbelserion.fusion.files.exceptions;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class FileException extends IllegalStateException {

    public FileException(final String message, final Exception exception) {
        super(message, exception);
    }

    public FileException(final String message) {
        super(message);
    }
}