package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.kyori.enums.LoggerType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
@FunctionalInterface
public interface ILogger {

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the {@link LoggerType}
     * @param message the message to send
     * @param args    the args
     */
    void log(@NotNull LoggerType type, @NotNull String message, @NotNull Object... args);

    /**
     * Sends a warning log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void warn(@NotNull String message, @NotNull Object... args) {
        log(LoggerType.WARNING, message, args);
    }

    /**
     * Sends an error log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void error(@NotNull String message, @NotNull Object... args) {
        log(LoggerType.ERROR, message, args);
    }

    /**
     * Sends a safe log message parsed with MiniMessage.
     *
     * @param message the message to send
     * @param args    the args
     */
    default void safe(@NotNull String message,  Object... args) {
        log(LoggerType.SAFE, message, args);
    }
}