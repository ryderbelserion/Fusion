package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.enums.LoggerType;

public interface ILogger {

    void log(final LoggerType type, final String message, final Object... args);

    default void warn(final String message, final Object... args) {
        log(LoggerType.WARNING, message, args);
    }

    default void error(final String message, final Object... args) {
        log(LoggerType.ERROR, message, args);
    }

    default void safe(final String message, final Object... args) {
        log(LoggerType.SAFE, message, args);
    }
}