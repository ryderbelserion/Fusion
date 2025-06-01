package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
public interface ILogger {

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the {@link LoggerType}
     * @param message   the message to send
     * @param throwable the throwable
     */
    void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Throwable throwable);

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the {@link LoggerType}
     * @param message the message to send
     * @param args    the args
     */
    void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args);

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    default void warn(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.WARNING, message, throwable);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    default void error(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.ERROR, message, throwable);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message   the message to send
     * @param throwable the throwable
     */
    default void safe(@NotNull final String message, @NotNull final Throwable throwable) {
        log(LoggerType.SAFE, message, throwable);
    }

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void warn(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.WARNING, message, args);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void error(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.ERROR, message, args);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void safe(@NotNull final String message, final Object... args) {
        log(LoggerType.SAFE, message, args);
    }

    /**
     * Available logger types when using {@code ILogger#log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args) }
     */
    enum LoggerType {
        /**
         * Safe logging type!
         */
        SAFE,
        /**
         * Shady logging type!
         */
        WARNING,
        /**
         * WEEWOO WEEWOO
         */
        ERROR
    }
}