package com.ryderbelserion.fusion.core.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * A custom exception to catch exceptions
 */
public final class FusionException extends IllegalStateException {

    /**
     * Throws an illegal state exception.
     *
     * @param message   the message to send
     * @param exception the exception to throw
     */
    public FusionException(@NotNull String message, @NotNull Exception exception) {
        super(message, exception);
    }

    /**
     * Throws an illegal state exception.
     *
     * @param s the message to send
     */
    public FusionException(@NotNull String s) {
        super(s);
    }
}