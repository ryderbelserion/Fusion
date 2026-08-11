package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import java.nio.file.Path;

@NullMarked
public final class FusionVelocity extends FusionKyori<Audience> {

    private final ComponentLogger logger;
    private final String namespace;

    public FusionVelocity(final String namespace, final ComponentLogger logger, final Path path) {
        super(path);

        this.logger = logger;
        this.namespace = namespace;
    }

    @Override
    public void log(final Level level, final String message, final Exception exception, final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case warn -> this.logger.warn(component, exception);
            case error -> this.logger.error(component, exception);
            case info -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(final Level level, final String message, final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case warn -> this.logger.warn(component);
            case error -> this.logger.error(component);
            case info -> this.logger.info(component);
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