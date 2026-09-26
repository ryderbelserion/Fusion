package com.ryderbelserion.fusion.addons.exceptions;

import org.jspecify.annotations.NonNull;

public class InvalidExtensionException extends Exception {

    public InvalidExtensionException(@NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
    }

    public InvalidExtensionException(@NonNull final String message) {
        super(message);
    }
}