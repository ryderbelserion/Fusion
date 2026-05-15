package com.ryderbelserion.fusion.files;

import org.jspecify.annotations.NonNull;

public class FileException extends IllegalStateException {

    public FileException(@NonNull final String message, @NonNull final Exception exception) {
        super(message, exception);
    }

    public FileException(@NonNull final String message) {
        super(message);
    }
}