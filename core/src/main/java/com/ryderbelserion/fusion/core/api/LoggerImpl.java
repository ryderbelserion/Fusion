package com.ryderbelserion.fusion.core.api;

import com.ryderbelserion.fusion.api.interfaces.ILogger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class LoggerImpl implements ILogger {

    private final ComponentLogger logger;

    public LoggerImpl(final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(final String message) {
        this.logger.info(message);
    }

    @Override
    public void info(final String message, final Object argument) {
        this.logger.info(message, argument);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        this.logger.info(message, throwable);
    }

    @Override
    public void info(final String message, final Object... arguments) {
        this.logger.info(message, arguments);
    }

    @Override
    public void warn(final String message) {
        this.logger.warn(message);
    }

    @Override
    public void warn(final String message, final Object argument) {
        this.logger.warn(message, argument);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        this.logger.warn(message, throwable);
    }

    @Override
    public void warn(final String message, final Object... arguments) {
        this.logger.warn(message, arguments);
    }

    @Override
    public void severe(final String message) {
        this.logger.error(message);
    }

    @Override
    public void severe(final String message, final Object argument) {
        this.logger.error(message, argument);
    }

    @Override
    public void severe(final String message, final Throwable throwable) {
        this.logger.error(message, throwable);
    }

    @Override
    public void severe(final String message, final Object... arguments) {
        this.logger.error(message, arguments);

        this.logger.error("{}", message);
    }
}