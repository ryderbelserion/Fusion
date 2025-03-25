package com.ryderbelserion.fusion.api.interfaces;

public interface ILogger {

    void info(final String message);

    void info(final String message, final Object argument);

    void info(final String message, final Throwable throwable);

    void info(final String message, final Object... arguments);

    void warn(final String message);

    void warn(final String message, final Object argument);

    void warn(final String message, final Throwable throwable);

    void warn(final String message, final Object... arguments);

    void severe(final String message);

    void severe(final String message, final Object argument);

    void severe(final String message, final Throwable throwable);

    void severe(final String message, final Object... arguments);

}