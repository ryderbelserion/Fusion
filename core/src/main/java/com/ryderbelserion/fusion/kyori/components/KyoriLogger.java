package com.ryderbelserion.fusion.kyori.components;

import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
public class KyoriLogger extends ILogger {

    private final ComponentLogger logger;
    private final FusionKyori kyori;

    /**
     * Builds a custom implementation of the logger for multiple platforms.
     *
     * @param logger instance of {@link ComponentLogger}
     * @param kyori instance of {@link FusionKyori}
     */
    public KyoriLogger(@NotNull final ComponentLogger logger, @NotNull final FusionKyori kyori) {
        this.logger = logger;
        this.kyori = kyori;
    }

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the {@link LoggerType}
     * @param message   the message to send
     * @param throwable the throwable
     */
    @Override
    public void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Throwable throwable) {
        if (!this.kyori.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case SAFE -> this.logger.info(component, throwable);
            case ERROR -> this.logger.error(component, throwable);
            case WARNING -> this.logger.warn(component, throwable);
        }
    }

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the {@link LoggerType}
     * @param message the message to send
     * @param args    the args
     */
    @Override
    public void log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args) {
        if (!this.kyori.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case SAFE -> this.logger.info(component, args);
            case ERROR -> this.logger.error(component, args);
            case WARNING -> this.logger.warn(component, args);
        }
    }
}