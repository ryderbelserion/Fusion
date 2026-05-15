package com.ryderbelserion.fusion.core.api.exceptions;

import org.jspecify.annotations.NonNull;

public class FusionException extends IllegalStateException {

    public FusionException(@NonNull final String message, @NonNull final Exception exception) {
        super(message, exception);
    }

    public FusionException(@NonNull final String message) {
        super(message);
    }
}