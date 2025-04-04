package com.ryderbelserion.fusion.discord.managers;

import ch.qos.logback.classic.Logger;
import com.ryderbelserion.fusion.api.interfaces.ILogger;

public class LoggerManager implements ILogger {

    private final Logger logger;

    public LoggerManager(final Logger logger) {
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
    }
}