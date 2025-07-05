package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
public abstract class ILogger {

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the name of the logger level
     * @param message   the message to send
     * @param throwable the throwable
     */
    protected abstract void log(@NotNull final String type, @NotNull final String message, @NotNull final Throwable throwable);

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the name of the logger level
     * @param message the message to send
     * @param args    the args
     */
    protected abstract void log(@NotNull final String type, @NotNull final String message, @NotNull final Object... args);

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void warn(@NotNull final String message, @NotNull final Throwable throwable) {
        log("warning", message, throwable);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void error(@NotNull final String message, @NotNull final Throwable throwable) {
        log("error", message, throwable);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void safe(@NotNull final String message, @NotNull final Throwable throwable) {
        log("safe", message, throwable);
    }

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void warn(@NotNull final String message, @NotNull final Object... args) {
        log("warn", message, args);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void error(@NotNull final String message, @NotNull final Object... args) {
        log("error", message, args);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void safe(@NotNull final String message, final Object... args) {
        log("safe", message, args);
    }
}