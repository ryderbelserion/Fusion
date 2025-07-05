package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.utils.AdvUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Handles logger messages for multiple platforms.
 */
public class FusionLogger extends ILogger {

    private final FusionCore fusion = FusionProvider.get();

    private final ComponentLogger logger;

    /**
     * Builds a custom implementation of the logger for multiple platforms.
     *
     * @param logger instance of {@link ComponentLogger}
     */
    public FusionLogger(@NotNull final ComponentLogger logger) {
        this.logger = logger;
    }

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the name of the logger level
     * @param message   the message to send
     * @param throwable the throwable
     */
    @Override
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Throwable throwable) {
        if (this.fusion.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case "safe" -> this.logger.info(component, throwable);
            case "error" -> this.logger.error(component, throwable);
            case "warm" -> this.logger.warn(component, throwable);
        }
    }

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the name of the logger level
     * @param message the message to send
     * @param args    the args
     */
    @Override
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Object... args) {
        if (this.fusion.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case "safe" -> this.logger.info(component, args);
            case "error" -> this.logger.error(component, args);
            case "warm" -> this.logger.warn(component, args);
        }
    }
}