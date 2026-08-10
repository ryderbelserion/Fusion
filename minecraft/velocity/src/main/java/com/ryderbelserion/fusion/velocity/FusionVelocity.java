package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import java.nio.file.Path;

@NullMarked
public final class FusionVelocity extends FusionKyori<Audience, FileManager> {

    private final ComponentLogger logger;
    private final String namespace;

    public FusionVelocity(final String namespace, final ComponentLogger logger, final Path path) {
        super(new FileManager(path), path);

        this.logger = logger;
        this.namespace = namespace;
    }

    @Override
    public void log(final Level level, final String message, final Exception exception, final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component, exception);
            case DEBUG -> this.logger.debug(component, exception);
            case ERROR -> this.logger.error(component, exception);
            case INFO -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(final Level level, final String message, final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component);
            case DEBUG -> this.logger.debug(component);
            case ERROR -> this.logger.error(component);
            case INFO -> this.logger.info(component);
        }
    }

    @Override
    public boolean isModReady(final FusionKey key) {
        return false;
    }

    @Override
    public boolean isModReady(final String key) {
        return false;
    }

    @Override
    public String getNamespace() {
        return this.namespace.toLowerCase();
    }

    @Override
    public String papi(final Audience sender, final String message) {
        return message;
    }
}