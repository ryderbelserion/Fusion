package com.ryderbelserion.fusion.core.api;

import com.ryderbelserion.fusion.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class LoggerImpl implements ILogger {

    private final ComponentLogger logger;

    public LoggerImpl(final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(final String message) {
        this.logger.info(AdvUtils.parse(message));
    }

    @Override
    public void info(final String message, final Object argument) {
        this.logger.info(AdvUtils.parse(message), argument);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        this.logger.info(AdvUtils.parse(message), throwable);
    }

    @Override
    public void info(final String message, final Object... arguments) {
        this.logger.info(AdvUtils.parse(message), arguments);
    }

    @Override
    public void warn(final String message) {
        this.logger.warn(AdvUtils.parse(message));
    }

    @Override
    public void warn(final String message, final Object argument) {
        this.logger.warn(AdvUtils.parse(message), argument);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        this.logger.warn(AdvUtils.parse(message), throwable);
    }

    @Override
    public void warn(final String message, final Object... arguments) {
        this.logger.warn(AdvUtils.parse(message), arguments);
    }

    @Override
    public void severe(final String message) {
        this.logger.error(AdvUtils.parse(message));
    }

    @Override
    public void severe(final String message, final Object argument) {
        this.logger.error(AdvUtils.parse(message), argument);
    }

    @Override
    public void severe(final String message, final Throwable throwable) {
        this.logger.error(AdvUtils.parse(message), throwable);
    }

    @Override
    public void severe(final String message, final Object... arguments) {
        this.logger.error(AdvUtils.parse(message), arguments);
    }
}