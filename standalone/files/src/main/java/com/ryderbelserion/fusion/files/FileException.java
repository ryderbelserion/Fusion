package com.ryderbelserion.fusion.files;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FileException extends IllegalStateException {

    public FileException(final String message, final Exception exception) {
        super(message, exception);
    }

    public FileException(final String message) {
        super(message);
    }
}