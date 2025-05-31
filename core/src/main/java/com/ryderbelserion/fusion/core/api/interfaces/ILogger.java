package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import org.jetbrains.annotations.NotNull;

public interface ILogger {

    void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args);

    default void warn(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.WARNING, message, args);
    }

    default void error(@NotNull final String message, @NotNull final Object... args) {
        log(LoggerType.ERROR, message, args);
    }

    default void safe(@NotNull final String message, final Object... args) {
        log(LoggerType.SAFE, message, args);
    }
}