package com.ryderbelserion.fusion.api.exceptions;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class FusionException extends IllegalStateException {

    public FusionException(final String message, final Exception exception) {
        super(message, exception);
    }

    public FusionException(final String message) {
        super(message);
    }
}