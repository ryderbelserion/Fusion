package com.ryderbelserion.fusion.core.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * A custom exception to catch exceptions
 */
public class FusionException extends IllegalStateException {

    /**
     * Throws an illegal state exception.
     *
     * @param message   the message to send
     * @param exception the exception to throw
     */
    public FusionException(@NotNull final String message, @NotNull final Exception exception) {
        super(message, exception);
    }

    /**
     * Throws an illegal state exception.
     *
     * @param message the message to send
     */
    public FusionException(@NotNull final String message) {
        super(message);
    }
}