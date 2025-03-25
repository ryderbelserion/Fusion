package com.ryderbelserion.fusion.api.exceptions;

public final class FusionException extends IllegalStateException {

    public FusionException(final String message, final Exception exception) {
        super(message, exception);
    }

    public FusionException(final String message) {
        super(message);
    }
}