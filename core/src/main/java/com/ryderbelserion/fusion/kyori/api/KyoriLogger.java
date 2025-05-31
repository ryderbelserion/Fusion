package com.ryderbelserion.fusion.kyori.api;

import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

public class KyoriLogger implements ILogger {

    private final ComponentLogger logger;

    public KyoriLogger(@NotNull final ComponentLogger logger) {
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