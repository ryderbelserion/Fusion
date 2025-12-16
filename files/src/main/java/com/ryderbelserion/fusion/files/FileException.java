package com.ryderbelserion.fusion.files;

import org.jetbrains.annotations.NotNull;

public class FileException extends IllegalStateException {

    public FileException(@NotNull final String message, @NotNull final Exception exception) {
        super(message, exception);
    }

    public FileException(@NotNull final String message) {
        super(message);
    }
}