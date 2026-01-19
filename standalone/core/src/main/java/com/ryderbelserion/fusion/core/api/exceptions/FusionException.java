package com.ryderbelserion.fusion.core.api.exceptions;

import org.jetbrains.annotations.NotNull;

public class FusionException extends IllegalStateException {

    public FusionException(@NotNull final String message, @NotNull final Exception exception) {
        super(message, exception);
    }

    public FusionException(@NotNull final String message) {
        super(message);
    }
}