package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
public abstract class ILogger {

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the {@link LoggerType}
     * @param message   the message to send
     * @param throwable the throwable
     */
    public abstract void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Throwable throwable);

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the {@link LoggerType}
     * @param message the message to send
     * @param args    the args
     */
    public abstract void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args);

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void warn(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.WARNING, message, throwable);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void error(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.ERROR, message, throwable);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void safe(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.SAFE, message, throwable);
    }

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void warn(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.WARNING, message, args);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void error(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.ERROR, message, args);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    public void safe(@NotNull final String message, final Object... args) {
        log(LoggerType.SAFE, message, args);
    }
}