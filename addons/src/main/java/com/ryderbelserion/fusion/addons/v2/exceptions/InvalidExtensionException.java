package com.ryderbelserion.fusion.addons.v2.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidExtensionException extends Exception {

    public InvalidExtensionException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }

    public InvalidExtensionException(@NotNull final String message) {
        super(message);
    }
}