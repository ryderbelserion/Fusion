package com.ryderbelserion.fusion.adventure.api;

import com.ryderbelserion.fusion.adventure.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

public class AdventureLogger implements ILogger {

    private final ComponentLogger logger;

    public AdventureLogger(@NotNull final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public void log(final LoggerType type, final String message, final Object... args) {
        switch (type) {
            case SAFE -> this.logger.info(AdvUtils.parse(message), args);
            case ERROR -> this.logger.error(AdvUtils.parse(message), args);
            case WARNING -> this.logger.warn(AdvUtils.parse(message), args);
        }
    }
}