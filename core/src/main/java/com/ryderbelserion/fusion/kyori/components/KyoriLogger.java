package com.ryderbelserion.fusion.kyori.components;

import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.enums.LoggerType;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

public class KyoriLogger implements ILogger {

    private final ComponentLogger logger;

    public KyoriLogger(@NotNull final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public void log(final LoggerType type, final String message, final Object... args) {
        final Component component = AdvUtils.parse(message);

        switch (type) {
            case SAFE -> this.logger.info(component, args);
            case ERROR -> this.logger.error(component, args);
            case WARNING -> this.logger.warn(component, args);
        }
    }
}